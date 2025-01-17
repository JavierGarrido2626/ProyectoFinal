document.addEventListener('DOMContentLoaded', () => {
    // Manejo del formulario de inicio de sesión
    const inicioForm = document.getElementById('loginformulario');
    const mensajeLogin = document.getElementById('mensajelogin'); 

    if (inicioForm) {
        inicioForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const nombreUsuario = document.getElementById('nombreUsuario').value;
            const contrasena = document.getElementById('contrasena').value;

            try {
                const response = await fetch('http://localhost:5000/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ usuario: nombreUsuario, contrasena: contrasena }),
                });

                if (mensajeLogin) {
                    if (response.ok) {
                        const data = await response.json();
                        // Guardar id_usuario en localStorage
                        localStorage.setItem('id_usuario', data.id_usuario);  
                        localStorage.setItem('nombreUsuario', nombreUsuario);  
                        mensajeLogin.textContent = 'Inicio de sesión exitoso';
                        mensajeLogin.style.color = 'green';
                        console.log("Redirigiendo al inicio...");
                        window.location.href = 'pages/Inicio.html';  
                    } else {
                        const data = await response.json();
                        mensajeLogin.textContent = data.error || 'Error al iniciar sesión';
                        mensajeLogin.style.color = 'red';
                    }
                }
            } catch (error) {
                console.error('Error:', error);
                if (mensajeLogin) {
                    mensajeLogin.textContent = 'Error al conectar con el servidor.';
                    mensajeLogin.style.color = 'red';
                }
            }
        });
    }

    // Verificar si el usuario está en modo invitado o ha iniciado sesión
    const nombreUsuario = localStorage.getItem('nombreUsuario');
    const modoInvitado = localStorage.getItem('modoInvitado'); 

    if (modoInvitado) {
        // Si está en modo invitado, mostrar el mensaje correspondiente
        document.getElementById('nombreUsuario').textContent = ' Invitado';
        
        // Ocultar los enlaces de Login y Registro
        const loginLinks = document.getElementById('loginLinks');
        loginLinks.style.display = 'none'; 

        // Mostrar el botón de Cerrar sesión
        const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
        cerrarSesionBtn.style.display = 'inline'; 
    } else if (nombreUsuario) {
        // Si hay un nombre de usuario, mostrar el nombre de usuario
        document.getElementById('nombreUsuario').textContent = nombreUsuario;

        // Mostrar el mensaje de bienvenida
        const loginLinks = document.getElementById('loginLinks');
        loginLinks.style.display = 'none'; 

        // Mostrar el botón de Cerrar sesión
        const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
        cerrarSesionBtn.style.display = 'inline'; 
    }

    // Función para manejar el "Modo Invitado"
    const modoInvitadoLink = document.getElementById('modoInvitado');
    if (modoInvitadoLink) {
        modoInvitadoLink.addEventListener('click', function(event) {
            event.preventDefault(); 

            // Guardar un valor temporal en localStorage para identificar que está como invitado
            localStorage.setItem('modoInvitado', true);

            // Mostrar el mensaje de bienvenida para el invitado
            document.getElementById('nombreUsuario').textContent = ' Invitado';

            // Ocultar los enlaces de inicio de sesión y registro
            const loginLinks = document.getElementById('loginLinks');
            loginLinks.style.display = 'none';

            // Mostrar el botón de Cerrar sesión
            const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
            cerrarSesionBtn.style.display = 'inline'; // Hacer visible el botón

            
            setTimeout(() => {
                console.log("Redirigiendo a Inicio.html...");
                window.location.href = 'pages/Inicio.html';  
            }, 200);
        });
    }

    // Función de Cerrar sesión
    const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
    if (cerrarSesionBtn) {
        cerrarSesionBtn.addEventListener('click', () => {
            // Eliminar el usuario del localStorage y recargar la página
            localStorage.removeItem('nombreUsuario');
            localStorage.removeItem('id_usuario');  
            localStorage.removeItem('modoInvitado'); 
            console.log("Cerrando sesión y redirigiendo...");
            window.location.href = './Inicio.html'; 
        });
    }
});
