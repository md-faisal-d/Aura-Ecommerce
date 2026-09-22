const ctx = window.APP_CONTEXT || "";

function formatMoney(value) {
    const n = Number(value);
    return isNaN(n) ? value : n.toFixed(2).replace(/\.?0+$/, "") || n;
}

function updateCartUi(data) {
    const activeIds = new Set(data.items.map((i) => String(i.variantId)));

    document.querySelectorAll(".cart-card").forEach((card) => {
        const id = card.getAttribute("data-variant-id");
        if (!activeIds.has(id)) {
            card.style.opacity = "0";
            card.style.transform = "scale(0.98)";
            setTimeout(() => card.remove(), 200);
        }
    });

    data.items.forEach((item) => {
        const qtyEl = document.getElementById("qty-" + item.variantId);
        const subtotalEl = document.getElementById("subtotal-" + item.variantId);
        if (qtyEl) qtyEl.textContent = item.quantity;
        if (subtotalEl) subtotalEl.textContent = "₹" + formatMoney(item.subtotal);
    });

    const grandTotal = document.getElementById("grandTotal");
    if (grandTotal) grandTotal.textContent = "₹" + formatMoney(data.total);

    const countEl = document.getElementById("summaryCount");
    if (countEl) countEl.textContent = data.lineCount ?? data.items.length;

    const badge = document.querySelector(".cart-badge");
    if (badge) {
        badge.textContent = data.count;
        badge.style.display = data.count > 0 ? "inline-flex" : "none";
    }

    if (data.items.length === 0) {
        setTimeout(() => window.location.reload(), 250);
    }
}

async function cartAction(action, variantId) {
    if (!variantId) return;

    try {
        const body = new URLSearchParams();
        body.append("action", action);
        body.append("variantId", variantId);

        const response = await fetch(ctx + "/cart-api", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: body.toString(),
            credentials: "same-origin"
        });

        if (!response.ok) throw new Error("Cart update failed");

        const data = await response.json();
        updateCartUi(data);
    } catch (err) {
        console.error(err);
        window.location.href = ctx + "/remove-cart-item?variantId=" + variantId;
    }
}

const cartRoot = document.getElementById("cartItems");
if (cartRoot) {
    cartRoot.addEventListener("click", (e) => {
        const btn = e.target.closest(".qty-btn, .remove-btn");
        if (!btn) return;
        e.preventDefault();
        cartAction(btn.dataset.action, btn.dataset.variant);
    });
}
