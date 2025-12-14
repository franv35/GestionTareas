document.getElementById("btnLogin").addEventListener("click", async () => {
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!email || !password) {
    alert("Por favor, completá ambos campos.");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/usuarios/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      throw new Error("Credenciales inválidas");
    }

    const data = await response.json();

    // 🔐 Guardamos token
    localStorage.setItem("token", data.token);
    localStorage.setItem("usuarioId", data.id);
	localStorage.setItem("usuarioEmail", data.email);
    localStorage.setItem("usuarioNombre", data.nombre);

    // ✅ Redirección correcta según tu flujo
    window.location.href = "proyectos.html";

  } catch (error) {
    alert("Error al iniciar sesión");
    console.error(error);
  }
});
