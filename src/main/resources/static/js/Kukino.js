document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Frontend załadowany!");
    checkLoginStatus();
    checkLoginStatus();
    loadPopularRecipes(); // Ładowanie najpopularniejszych przepisów
});

//Rejestracja
function register() {
    const username = document.getElementById("register-username").value.trim();
    const password = document.getElementById("register-password").value.trim();
    
    if (!username || !password) {
        document.getElementById("register-message").innerText = "⚠️ Podaj nazwę użytkownika i hasło!";
        return;
    }

    fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
    .then(response => response.text())
    .then(message => {
        document.getElementById("register-message").innerText = message;
    })
    .catch(error => console.error("❌ Błąd rejestracji", error));
}

// Logowanie
function login() {
    const username = document.getElementById("login-username").value.trim();
    const password = document.getElementById("login-password").value.trim();

    if (!username || !password) {
        document.getElementById("login-message").innerText = "⚠️ Podaj nazwę użytkownika i hasło!";
        return;
    }

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
    .then(response => {
        if (!response.ok) throw new Error("Błędne dane logowania!");
        return response.text();
    })
    .then(message => {
        document.getElementById("login-message").innerText = message;
        checkLoginStatus();
    })
    .catch(error => {
        document.getElementById("login-message").innerText = "❌ Niepoprawne dane!";
        console.error("❌ Błąd logowania", error);
    });
}

//Sprawdzanie logowania
function checkLoginStatus() {
    fetch("/api/auth/status")
    .then(response => {
        if (!response.ok) throw new Error("Nie zalogowano");
        return response.text();
    })
    .then(username => {
        document.getElementById("user-panel").style.display = "block";
        document.getElementById("login-container").style.display = "none";
        document.getElementById("register-container").style.display = "none";
        document.getElementById("user-name").innerText = username.replace("✅ Zalogowany jako: ", "");
        
        console.log("🔄 Wczytywanie moich przepisów...");
        loadUserRecipes(); // AUTOMATYCZNE WYCZYTANIE PRZEPISÓW PO ZALOGOWANIU!
    })
    .catch(() => {
        document.getElementById("user-panel").style.display = "none";
        document.getElementById("login-container").style.display = "block";
        document.getElementById("register-container").style.display = "block";
    });
}

// Wylogowywanie
function logout() {
    fetch("/api/auth/logout", { method: "POST" })
    .then(() => {
        document.getElementById("login-message").innerText = "👋 Wylogowano!";
        checkLoginStatus();
    })
    .catch(error => console.error("❌ Błąd wylogowania", error));
}

// Zmiana hasła
function changePassword() {
    const newPassword = document.getElementById("new-password").value.trim();
    if (!newPassword) {
        document.getElementById("password-message").innerText = "⚠️ Podaj nowe hasło!";
        return;
    }

    fetch("/api/user/change-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ password: newPassword })
    })
    .then(response => response.text())
    .then(message => {
        document.getElementById("password-message").innerText = message;
    })
    .catch(error => console.error("❌ Błąd zmiany hasła", error));
}

//Usuwanie konta
function deleteAccount() {
    if (!confirm("⚠️ Na pewno chcesz usunąć konto? Tej operacji nie można cofnąć!")) {
        return;
    }

    fetch("/api/user/delete", { method: "DELETE" })
    .then(response => response.text())
    .then(message => {
        document.getElementById("delete-message").innerText = message;
        setTimeout(() => logout(), 2000); // Wylogowanie po usunięciu konta
    })
    .catch(error => console.error("❌ Błąd usuwania konta", error));
}

//Dodawanie przepisu
function addRecipe() {
    const title = document.getElementById("recipe-title").value.trim();
    const ingredients = document.getElementById("recipe-ingredients").value.trim().split(",");
    const instructions = document.getElementById("recipe-instructions").value.trim();

    if (!title || ingredients.length === 0 || !instructions) {
        document.getElementById("recipe-message").innerText = "⚠️ Uzupełnij wszystkie pola!";
        return;
    }

    fetch("/api/recipes/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, ingredients, instructions })
    })
    .then(response => response.text())
    .then(message => {
        document.getElementById("recipe-message").innerText = message;
        loadUserRecipes(); // Odśwież listę przepisów
    })
    .catch(error => console.error("❌ Błąd dodawania przepisu", error));
}

//Wczytywanie przepisów użytkownika
function loadUserRecipes() {
    fetch("/api/recipes/my")
    .then(response => response.json())
    .then(recipes => {
        const list = document.getElementById("user-recipes");
        list.innerHTML = ""; // 🔄 Wyczyść poprzednie wyniki

        if (!recipes || recipes.length === 0) {
            list.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak przepisów</li>";
            return;
        }

        recipes.forEach(recipe => {
            const listItem = document.createElement("li");
            listItem.className = "list-group-item";
            listItem.innerHTML = `
                <strong>${recipe.title}</strong><br>
                🥘 Składniki: ${recipe.ingredients.join(", ")}<br>
                📜 Instrukcje: ${recipe.instructions}<br>
                <button onclick="editRecipe('${recipe.id}')" class="btn btn-primary">✏️ Edytuj</button>
                <button onclick="deleteRecipe('${recipe.id}')" class="btn btn-danger">❌ Usuń</button>
            `;
            list.appendChild(listItem);
        });
    })
    .catch(error => console.error("❌ Błąd ładowania moich przepisów", error));
}

// Edycja przepisów
function editRecipe(recipeId) {
    const newTitle = prompt("Nowy tytuł:");
    const newIngredients = prompt("Składniki (po przecinku):");
    const newInstructions = prompt("Instrukcje:");

    // Jeśli user kliknie „Anuluj” w prompt, przerwij
    if (newTitle === null || newIngredients === null || newInstructions === null) return;

    const updatedRecipe = {
        title: newTitle,
        ingredients: newIngredients.split(","),
        instructions: newInstructions
    };

    fetch(`/api/recipes/edit/${recipeId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedRecipe)
    })
    .then(response => response.text())
    .then(msg => {
        alert(msg);
        loadUserRecipes(); // Odśwież listę po edycji
    })
    .catch(err => console.error("❌ Błąd edycji przepisu", err));
}

//Usuwanie przepisu
function deleteRecipe(recipeId) {
    if (!confirm("⚠️ Na pewno chcesz usunąć ten przepis?")) return;

    fetch(`/api/recipes/delete/${recipeId}`, { method: "DELETE" })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadUserRecipes(); // Odśwież listę przepisów
    })
    .catch(error => console.error("❌ Błąd usuwania przepisu", error));
}

// Wczytywanie popularnych przepisów
function loadPopularRecipes() {
    fetch("/api/recipes/popular")
        .then(response => response.json())
        .then(recipes => {
            const list = document.getElementById("popularRecipesList");
            list.innerHTML = ""; // Wyczyść listę
            if (!recipes || recipes.length === 0) {
                list.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak popularnych przepisów</li>";
                return;
            }
            recipes.forEach(recipe => {
                const listItem = document.createElement("li");
                listItem.className = "list-group-item";
                
                const titleLink = document.createElement("a");
                titleLink.href = "#";
                titleLink.textContent = recipe.title;
                titleLink.style.fontWeight = "bold";
                titleLink.addEventListener("click", function(e) {
                    e.preventDefault();
                    viewRecipeDetails(recipe.id);
                });
                
                const ingredientsDiv = document.createElement("div");
                ingredientsDiv.innerHTML = "🥘 Składniki: " + recipe.ingredients.join(", ");
                
                const popularitySpan = document.createElement("span");
                popularitySpan.textContent = " Wyświetlenia: " + recipe.popularity;
                
                listItem.appendChild(titleLink);
                listItem.appendChild(document.createElement("br"));
                listItem.appendChild(ingredientsDiv);
                listItem.appendChild(document.createElement("br"));
                listItem.appendChild(popularitySpan);
                
                list.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error("❌ Błąd pobierania popularnych przepisów", error);
            alert("🚨 Błąd połączenia z serwerem!");
        });
}

// Szczegóły przepisu
function viewRecipeDetails(recipeId) {
    fetch(`/api/recipes/view/${recipeId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Nie znaleziono przepisu (status: ${response.status})`);
            }
            return response.json();
        })
        .then(recipe => {
            if (!recipe || !recipe.ingredients) {   
                throw new Error("Brak danych przepisu");
            }
            alert(
                `Tytuł: ${recipe.title}\n` +
                `Składniki: ${recipe.ingredients.join(", ")}\n` +
                `Instrukcje: ${recipe.instructions}\n` +
                `Popularność: ${recipe.popularity}`
            );
            loadPopularRecipes();
        const detailsDiv = document.getElementById("recipeDetails");
            detailsDiv.innerHTML = `
              <h3>${recipe.title}</h3>
              <p>Składniki: ${recipe.ingredients.join(", ")}</p>
              <p>Instrukcje: ${recipe.instructions}</p>
              <button onclick="addToFavorites('${recipe.id}')">❤️ Dodaj do ulubionych</button>
            `;
        })
        .catch(error => {
            console.error("❌ Błąd pobierania szczegółów przepisu:", error);
            alert("❌ " + error.message);
        });
}

// Dodawanie do Ulubionych
function addToFavorites(recipeId) {
    fetch(`/api/user/favorites/${recipeId}`, {
        method: "POST"
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
    })
    .catch(error => console.error("❌ Błąd dodawania do ulubionych", error));
}

//Usuwanie z Ulubionych
function removeFromFavorites(recipeId) {
    fetch(`/api/user/favorites/${recipeId}`, {
        method: "DELETE"
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        loadFavorites();
    })
    .catch(error => console.error("❌ Błąd usuwania z ulubionych", error));
}

// Wczytywanie Ulubionych
function loadFavorites() {
    fetch("/api/user/favorites")
    .then(response => {
        if(!response.ok) throw new Error("Błąd pobierania ulubionych");
        return response.json();
    })
    .then(recipes => {
        const favList = document.getElementById("favoritesList");
        favList.innerHTML = ""; 

        if(!recipes || recipes.length === 0) {
            favList.innerHTML = "<li class='list-group-item text-danger'>Brak ulubionych</li>";
            return;
        }

        recipes.forEach(recipe => {
            const listItem = document.createElement("li");
            listItem.className = "list-group-item";
        
            const titleLink = document.createElement("a");
            titleLink.href = "#"; 
            titleLink.textContent = recipe.title;
            titleLink.style.fontWeight = "bold";
            titleLink.addEventListener("click", function(e) {
                e.preventDefault(); 
                viewRecipeDetails(recipe.id);
            });
        
            const ingredientsDiv = document.createElement("div");
            ingredientsDiv.textContent = "🥘 Składniki: " + recipe.ingredients.join(", ");
        
            // Przycisk „Usuń z ulubionych”
            const removeFavBtn = document.createElement("button");
            removeFavBtn.textContent = "❌ Usuń z ulubionych";
            removeFavBtn.className = "btn btn-danger btn-sm ms-2";
            removeFavBtn.addEventListener("click", () => removeFromFavorites(recipe.id));
        
            // Składanie elementów w liście
            listItem.appendChild(titleLink);
            listItem.appendChild(document.createElement("br"));
            listItem.appendChild(ingredientsDiv);
            listItem.appendChild(document.createElement("br"));
            listItem.appendChild(removeFavBtn);
        
            favList.appendChild(listItem);
        });
    })
    .catch(err => console.error("❌ Błąd ładowania ulubionych", err));
}

// Szukanie
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
            recipesList.innerHTML = "";

            if (recipes.length === 0) {
                recipesList.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak wyników</li>";
            } else {
                recipes.forEach(recipe => {
                    const listItem = document.createElement("li");
                    listItem.className = "list-group-item";
                    listItem.innerHTML = `
                        <strong>${recipe.title}</strong><br> 
                        🥘 Składniki: ${recipe.ingredients.join(", ")}<br>
                        📜 Instrukcje: ${recipe.instructions}<br>
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

window.searchRecipes = searchRecipes;