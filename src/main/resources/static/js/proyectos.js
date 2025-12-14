const API = "http://localhost:8080/api";

// ================= AUTH =================
const token = localStorage.getItem("token");
const usuarioId = localStorage.getItem("usuarioId");
const nombre = localStorage.getItem("usuarioNombre");
const email = localStorage.getItem("usuarioEmail");


if (!token || token.split(".").length !== 3) {
  console.warn("Token inválido o inexistente");
  localStorage.clear();
  window.location.href = "login.html";
}


document.getElementById("userInfo").innerText = `${nombre} · ${email}`;

// ================= ELEMENTOS =================
const proyectosGrid = document.getElementById("proyectosGrid");

const modalProyecto = document.getElementById("modalProyecto");
const modalRecurso = document.getElementById("modalRecurso");

// ================= MODALES =================
document.getElementById("btnAbrirProyecto").onclick = () =>
  modalProyecto.classList.remove("hidden");

document.getElementById("cerrarProyecto").onclick = () =>
  modalProyecto.classList.add("hidden");

document.getElementById("btnAbrirRecurso").onclick = () =>
  modalRecurso.classList.remove("hidden");

document.getElementById("cerrarRecurso").onclick = () =>
  modalRecurso.classList.add("hidden");

// ================= LOGOUT =================
document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.clear();
  window.location.href = "login.html";
});
// ================= PROYECTOS =================
async function cargarProyectos() {
  const res = await fetch(`${API}/usuarios/${usuarioId}/proyectos`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  const proyectos = await res.json();

  proyectosGrid.innerHTML = "";
  document.getElementById("statProyectos").innerText = proyectos.length;

  let enProceso = 0;
  let completadas = 0;

  proyectos.forEach(p => {
    if (p.tareas) {
      p.tareas.forEach(t => {
        if (t.estado === "EN_PROCESO") enProceso++;
        if (t.estado === "TERMINADA") completadas++;
      });
    }

    const card = document.createElement("div");
    card.className = "proyecto-card";
    card.innerHTML = `
      <h4>${p.nombre}</h4>
      <p>${p.descripcion || "Sin descripción"}</p>
    `;
    proyectosGrid.appendChild(card);
  });

  document.getElementById("statProceso").innerText = enProceso;
  document.getElementById("statCompletadas").innerText = completadas;
}

// ================= CREAR PROYECTO =================
document.getElementById("formProyecto").onsubmit = async e => {
  e.preventDefault();

  const body = {
    nombre: proyectoNombre.value,
    descripcion: proyectoDescripcion.value,
    fechaInicio: proyectoFecha.value
  };

  const res = await fetch(`${API}/proyectos/usuario/${usuarioId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(body)
  });

  if (!res.ok) {
    alert("Error al crear proyecto");
    return;
  }

  modalProyecto.classList.add("hidden");
  e.target.reset();
  cargarProyectos();
};

// ================= CREAR RECURSO =================
document.getElementById("formRecurso").onsubmit = async e => {
  e.preventDefault();

  const body = {
    nombre: recursoNombre.value,
    cantidad: recursoCantidad.value,
    unidad: recursoUnidad.value
  };

  const res = await fetch(`${API}/recursos`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(body)
  });

  if (!res.ok) {
    alert("Error al crear recurso");
    return;
  }

  modalRecurso.classList.add("hidden");
  e.target.reset();
};

// ================= INIT =================
cargarProyectos();
