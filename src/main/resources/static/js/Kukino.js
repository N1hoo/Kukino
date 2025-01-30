document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Frontend załadowany!");
});

function searchRecipes() {
    const query = document.getElementById("searchInput").value.trim();
    if (!query) {
        alert("❗ Wpisz nazwę przepisu lub składnik!");
        return;
    }

    console.log(`🔍 Wyszukiwanie przepisów dla: ${query}`);

    axios.get(`/api/recipes/search?query=${encodeURIComponent(query)}`)
        .then(response => {
            console.log("📡 Odpowiedź API:", response.data);
            const recipes = response.data;
            const recipesList = document.getElementById("recipesList");
            recipesList.innerHTML = ""; // Wyczyść poprzednie wyniki

            if (recipes.length === 0) {
                recipesList.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak wyników</li>";
            } else {
                recipes.forEach(recipe => {
                    const listItem = document.createElement("li");
                    listItem.className = "list-group-item";
                    listItem.innerHTML = `
                        <strong>${recipe.title}</strong><br> 
                        🥘 Składniki: ${recipe.ingredients.join(", ")}<br>
                        📜 Instrukcje: ${recipe.instructions}
                    `;
                    recipesList.appendChild(listItem);
                });
            }
        })
        .catch(error => {
            console.error("❌ Błąd pobierania przepisów", error);
            alert("🚨 Błąd połączenia z serwerem!");
        });
}

// Testowanie
window.searchRecipes = searchRecipes;
