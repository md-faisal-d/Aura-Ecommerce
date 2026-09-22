const ctx = window.APP_CONTEXT || "";

function toggleWishlist(productId) {
    const body = new URLSearchParams();
    body.append("productId", productId);
    body.append("redirect", window.location.pathname + window.location.search);

    fetch(ctx + "/wishlist", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: body.toString()
    }).then(() => window.location.reload());
}

document.querySelectorAll(".product-card").forEach((card, index) => {
    card.style.animationDelay = index * 0.05 + "s";
});
