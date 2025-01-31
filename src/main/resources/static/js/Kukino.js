document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Frontend załadowany!");
    checkLoginStatus();
});

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
    })
    .catch(() => {
        document.getElementById("user-panel").style.display = "none";
        document.getElementById("login-container").style.display = "block";
        document.getElementById("register-container").style.display = "block";
    });
}

function logout() {
    fetch("/api/auth/logout", { method: "POST" })
    .then(() => {
        document.getElementById("login-message").innerText = "👋 Wylogowano!";
        checkLoginStatus();
    })
    .catch(error => console.error("❌ Błąd wylogowania", error));
}
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

function searchRecipes() {
    const query = document.getElementById("searchInput").value.trim();
    if (!query) {
        alert("❗ Wpisz nazwę przepisu lub składnik!");
        return;
    }

    console.log(`🔍 Wyszukiwanie przepisów dla: ${query}`);

    fetch(`/api/recipes/search?query=${encodeURIComponent(query)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Błąd API: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("📡 Odpowiedź API:", data);
            const recipesList = document.getElementById("recipesList");
            recipesList.innerHTML = ""; // Wyczyść poprzednie wyniki

            if (data.length === 0) {
                recipesList.innerHTML = "<li class='list-group-item text-danger'>🚫 Brak wyników</li>";
            } else {
                data.forEach(recipe => {
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
            alert(`🚨 Błąd: ${error.message}`);
        });
}
window.searchRecipes = searchRecipes;