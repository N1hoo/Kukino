document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Frontend załadowany!");
});

function searchRecipes() {
    const query = document.getElementById("searchInput").value;
    if (!query) {
        alert("❗ Wpisz nazwę przepisu lub składnik!");
        return;
    }

    axios.get(`/api/recipes/search?query=${query}`)
        .then(response => {
            const recipes = response.data;
            const recipesList = document.getElementById("recipesList");
            recipesList.innerHTML = ""; // Wyczyść poprzednie wyniki

            if (recipes.length === 0) {
                recipesList.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak wyników</li>";
            } else {
                recipes.forEach(recipe => {
                    const listItem = document.createElement("li");
                    listItem.className = "list-group-item";
                    listItem.innerHTML = `<strong>${recipe.title}</strong> <br> 🥘 Składniki: ${recipe.ingredients.join(", ")}`;
                    recipesList.appendChild(listItem);
                });
            }
        })
        .catch(error => {
            console.error("❌ Błąd pobierania przepisów", error);
        });
}
