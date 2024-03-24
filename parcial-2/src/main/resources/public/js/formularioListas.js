function editarFormulario(button) {
    var nombre = button.getAttribute('data-nombre');
    var apellido = button.getAttribute('data-apellido');
    var educacion = button.getAttribute('data-educacion');
    var sector = button.getAttribute('data-sector');

    document.getElementById('nombreEditar').value = nombre;
    document.getElementById('apellidoEditar').value = apellido;
    document.getElementById('miComboBoxEditar').value = educacion;
    document.getElementById('sectorEditar').value = sector;
}

function guardarFormularioEditado() {
    var nombre = document.getElementById('nombreEditar').value;
    var apellido = document.getElementById('apellidoEditar').value;
    var educacion = document.getElementById('miComboBoxEditar').value;
    var sector = document.getElementById('sectorEditar').value;

    fetch('/formulario/editar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre: nombre,
            apellido: apellido,
            educacion: educacion,
            sector: sector
        })
    })
        .then(response => {
            alert('¡Formulario editado exitosamente!');
        })
        .catch(error => {
            console.error('Error al editar el formulario:', error);
        });
}

//Mapa
var map = L.map('mapid').setView([18.7357, -70.1627], 8);
var formularios = [];

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.marker([18.7357, -70.1627]).addTo(map)
    .bindPopup('Santo Domingo<br>Capital de República Dominicana')
    .openPopup();

for (var i = 0; i < formularios.length; i++) {
    var formulario = formularios[i];
    L.marker([formulario.latitud, formulario.longitud]).addTo(map)
        .bindPopup('Nombre: ' + formulario.nombre + '<br>' +
            'Apellido: ' + formulario.apellido);
}