/* ======================================================
   ELEMENTOS
====================================================== */

const optionCards = document.querySelectorAll(".option-card");
const modal = document.getElementById("infoModal");
const closeModalBtn = document.getElementById("closeModal");

const modalImg = document.getElementById("modalImg");
const modalTitle = document.getElementById("modalTitle");
const modalText = document.getElementById("modalText");

const btnGoRegister = document.getElementById("btnGoRegister");
const btnHeroRegister = document.getElementById("btnHeroRegister");

/* ======================================================
   AUTO REDIRECT SI YA LOGUEADO
====================================================== */

const token = localStorage.getItem("token");

if (token && token.split(".").length === 3) {
  window.location.href = "proyectos.html";
}

/* ======================================================
   CONTENIDO DINÁMICO DEL MODAL
====================================================== */

const modalContent = {
  estudio: {
    title: "Organizá tu estudio",
    text: "Planificá materias, fechas de exámenes y entregas sin perder el control. Visualizá tu progreso y mantené todo ordenado.",
    img: "img/estudio.jpg"
  },
  trabajo: {
    title: "Gestioná tu trabajo",
    text: "Administrá proyectos, tareas y recursos con una vista clara y profesional tipo tablero Kanban.",
    img: "img/trabajo.jpg"
  },
  personal: {
    title: "Organización personal",
    text: "Listas, hábitos, ideas y objetivos en un único espacio simple y eficiente.",
    img: "img/personal.jpg"
  }
};

/* ======================================================
   ABRIR MODAL
====================================================== */

function openModal(optionKey) {
  const content = modalContent[optionKey];
  if (!content) return;

  modalTitle.textContent = content.title;
  modalText.textContent = content.text;
  modalImg.src = content.img;

  modal.classList.remove("hidden");
}

/* ======================================================
   CERRAR MODAL
====================================================== */

function closeModal() {
  modal.classList.add("hidden");
}

/* ======================================================
   EVENTOS
====================================================== */

// Click en tarjetas
optionCards.forEach(card => {
  card.addEventListener("click", () => {
    const option = card.dataset.option;
    openModal(option);
  });
});

// Cerrar con X
closeModalBtn?.addEventListener("click", closeModal);

// Cerrar haciendo click fuera del contenido
modal?.addEventListener("click", e => {
  if (e.target === modal) {
    closeModal();
  }
});

// Botón crear cuenta (modal)
btnGoRegister?.addEventListener("click", () => {
  window.location.href = "registro.html";
});

// Botón hero
btnHeroRegister?.addEventListener("click", () => {
  window.location.href = "registro.html";
});