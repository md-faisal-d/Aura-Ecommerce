const ctx = window.APP_CONTEXT || document.querySelector("base")?.href || "";

const searchInput = document.getElementById("searchInput");
const suggestionsBox = document.getElementById("searchSuggestions");

if (searchInput && suggestionsBox) {
    let debounceTimer;

    searchInput.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        const query = searchInput.value.trim();

        if (query.length < 2) {
            suggestionsBox.innerHTML = "";
            suggestionsBox.classList.remove("open");
            return;
        }

        debounceTimer = setTimeout(async () => {
            const response = await fetch(
                (ctx || "") + "/api/search?q=" + encodeURIComponent(query)
            );
            const results = await response.json();
            renderSuggestions(results);
        }, 250);
    });

    document.addEventListener("click", (e) => {
        if (!e.target.closest(".search-box")) {
            suggestionsBox.classList.remove("open");
        }
    });
}

function renderSuggestions(results) {
    if (!results.length) {
        suggestionsBox.innerHTML = "<div class='suggestion-empty'>No products found</div>";
        suggestionsBox.classList.add("open");
        return;
    }

    suggestionsBox.innerHTML = results
        .map(
            (p) => `
        <a class="suggestion-item" href="${p.url}">
            <img src="${p.imageUrl}" alt="">
            <div>
                <strong>${p.name}</strong>
                <span>${p.brand} · ₹${p.price}</span>
            </div>
        </a>`
        )
        .join("");

    suggestionsBox.classList.add("open");
}
