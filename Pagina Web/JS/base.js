document.addEventListener('DOMContentLoaded', () => {
    const formLogin = document.getElementById('formLogin');
    const loginMessage = document.getElementById('loginMessage');

    if (formLogin) {
        formLogin.addEventListener('submit', async (e) => {
            e.preventDefault();
            const nombreUsuario = document.getElementById('nombreUsuario').value;
            const contrasena = document.getElementById('contrasena').value;

            try {
                const response = await fetch('http://localhost:5000/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ usuario: nombreUsuario, contrasena: contrasena }),
                });

                if (loginMessage) {
                    if (response.ok) {
                        loginMessage.textContent = 'Inicio de sesión exitoso';
                        loginMessage.style.color = 'green';
                        localStorage.setItem('nombreUsuario', nombreUsuario);
                        window.location.href = 'pages/Inicio.html';
                    } else {
                        const data = await response.json();
                        loginMessage.textContent = data.error || 'Error al iniciar sesión';
                        loginMessage.style.color = 'red';
                    }
                }
            } catch (error) {
                console.error('Error:', error);
                if (loginMessage) {
                    loginMessage.textContent = 'Error al conectar con el servidor.';
                    loginMessage.style.color = 'red';
                }
            }
        });
    }

    const formRegistro = document.getElementById('formRegistro');
    const registroMessage = document.getElementById('registroMessage');

    if (formRegistro) {
        formRegistro.addEventListener('submit', async (e) => {
            e.preventDefault();
            const nombre = document.getElementById('nombre').value;
            const apellido = document.getElementById('apellido').value;
            const nombreUsuario = document.getElementById('nombreUsuario').value;
            const contrasena = document.getElementById('contrasena').value;
            const correo = document.getElementById('correo').value;

            if (nombre.length < 3 || /[0-9!@#$%^&*(),.?":{}|<>áéíóúÁÉÍÓÚ]/.test(nombre)) {
                registroMessage.textContent = 'El nombre debe tener al menos 3 caracteres y no puede tener números ni caracteres especiales.';
                registroMessage.style.color = 'red';
                return;
            }

            if (apellido.length < 3 || /[0-9!@#$%^&*(),.?":{}|<>áéíóúÁÉÍÓÚ]/.test(apellido)) {
                registroMessage.textContent = 'El apellido debe tener al menos 3 caracteres y no puede tener números ni caracteres especiales.';
                registroMessage.style.color = 'red';
                return;
            }

            if (nombreUsuario.length < 3 || /[^a-zA-Z0-9]/.test(nombreUsuario)) {
                registroMessage.textContent = 'El nombre de usuario debe tener al menos 3 caracteres y solo puede contener letras y números.';
                registroMessage.style.color = 'red';
                return;
            }

            if (contrasena.length < 6 || !/[A-Z]/.test(contrasena) || !/[0-9]/.test(contrasena)) {
                registroMessage.textContent = 'La contraseña debe tener al menos 6 caracteres, una mayúscula y un número.';
                registroMessage.style.color = 'red';
                return;
            }

            try {
                const response = await fetch('http://localhost:5000/registrar_usuario', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        nombre,
                        apellidos: apellido,
                        usuario: nombreUsuario,
                        contrasena,
                        correo,
                    }),
                });

                if (registroMessage) {
                    if (response.ok) {
                        registroMessage.textContent = 'Usuario registrado exitosamente';
                        registroMessage.style.color = 'green';
                        formRegistro.reset();
                    } else {
                        const data = await response.json();
                        registroMessage.textContent = data.error || 'Error al registrar usuario';
                        registroMessage.style.color = 'red';
                    }
                }
            } catch (error) {
                console.error('Error:', error);
                if (registroMessage) {
                    registroMessage.textContent = 'Error al conectar con el servidor.';
                    registroMessage.style.color = 'red';
                }
            }
        });
    }

    const nombreUsuario = localStorage.getItem('nombreUsuario');
    if (nombreUsuario) {
        document.getElementById('nombreUsuarioDisplay').textContent = nombreUsuario;
        const loginLinks = document.getElementById('loginLinks');
        loginLinks.style.display = 'none';
        const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
        cerrarSesionBtn.style.display = 'inline';
    }

    const cerrarSesionBtn = document.getElementById('cerrarSesionBtn');
    if (cerrarSesionBtn) {
        cerrarSesionBtn.addEventListener('click', () => {
            localStorage.removeItem('nombreUsuario');
            window.location.href = '../Index.html';
        });
    }
});
