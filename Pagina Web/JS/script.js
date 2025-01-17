//Funciones para ir a los enlaces y que se haga mas grande las imagenes.

document.addEventListener('DOMContentLoaded', function () {
    var imagenBox1 = document.querySelector("#boxj1");

    imagenBox1.addEventListener("mouseover", function () {
        imagenBox1.style.transform = "scale(110%)";
    });

    imagenBox1.addEventListener("mouseout", function () {
        imagenBox1.style.transform = "scale(100%)";
    });

    imagenBox1.addEventListener('click', function () {
        window.location.href = "./Juego01.html";
    });

    /*---------------------------------------------------------------------------------------------*/

    var imagenBox2 = document.querySelector("#boxj2");

    imagenBox2.addEventListener("mouseover", function () {
        imagenBox2.style.transform = "scale(110%)";
    });

    imagenBox2.addEventListener("mouseout", function () {
        imagenBox2.style.transform = "scale(100%)";
    });

    imagenBox2.addEventListener('click', function () {
        window.location.href = "./Juego02.html";
    });

    /*---------------------------------------------------------------------------------------------*/

    var imagenBox3 = document.querySelector("#boxj3");

    imagenBox3.addEventListener("mouseover", function () {
        imagenBox3.style.transform = "scale(110%)";
    });

    imagenBox3.addEventListener("mouseout", function () {
        imagenBox3.style.transform = "scale(100%)";
    });

    imagenBox3.addEventListener('click', function () {
        window.location.href = "./Juego03.html";
    });

    /*---------------------------------------------------------------------------------------------*/

    var imagenBox4 = document.querySelector("#boxj4");

    imagenBox4.addEventListener("mouseover", function () {
        imagenBox4.style.transform = "scale(110%)";
    });

    imagenBox4.addEventListener("mouseout", function () {
        imagenBox4.style.transform = "scale(100%)";
    });

    imagenBox4.addEventListener('click', function () {
        window.location.href = "./Juego04.html";
    });
});





//***************************JUEGO 02***************************//
document.addEventListener('DOMContentLoaded', function () {
    const cajas = document.querySelectorAll('.DivBoxSectionJuego02');
    const contadorRondas = document.getElementById('contadorRondas');
    const contadorRondaActual = document.getElementById('contadorRondaActual');
    const contadorTiempo = document.getElementById('contadorTiempo');

    let secuencia = [];
    let secuenciaJugador = [];
    let nivelActual = 0;
    let velocidad = 1000;
    let juegoTerminado = false;
    let mostrandoSecuencia = false;
    let tiempo = 0;
    let intervaloTiempo;

    let nivelSeleccionado = ""; 
    const idUsuario = localStorage.getItem('id_usuario'); 

    // Asignar un nivel cuando el usuario hace clic en un nivel
    document.querySelectorAll('.nivel').forEach(boton => {
        boton.addEventListener('click', (evento) => {
            // Asignar la velocidad y el nivel seleccionado
            velocidad = parseInt(evento.target.dataset.velocidad);
            nivelSeleccionado = evento.target.dataset.nivel.toLowerCase(); 

            // Iniciar el juego
            iniciarJuego();
        });
    });

    // Función para iniciar el juego
    function iniciarJuego() {
        secuencia = [];
        secuenciaJugador = [];
        nivelActual = 0;
        juegoTerminado = false;
        tiempo = 0;
        clearInterval(intervaloTiempo);
        contadorRondas.textContent = "Rondas Completadas: 0";
        contadorRondaActual.textContent = "Ronda Actual: 0";
        contadorTiempo.textContent = "Tiempo: 0 segundos";

        // Iniciar el contador de tiempo
        intervaloTiempo = setInterval(() => {
            tiempo++;
            contadorTiempo.textContent = `Tiempo: ${tiempo} segundos`;
        }, 1000);

        siguienteRonda();
    }

    // Obtener una caja aleatoria para la secuencia
    function obtenerCajaAleatoria() {
        const indiceAleatorio = Math.floor(Math.random() * cajas.length);
        return cajas[indiceAleatorio];
    }

    // Mostrar la secuencia de botones
    function mostrarSecuencia() {
        mostrandoSecuencia = true;
        let i = 0;

        const intervalo = setInterval(() => {
            const caja = secuencia[i];
            resaltarCaja(caja);

            i++;
            if (i >= secuencia.length) {
                clearInterval(intervalo);
                mostrandoSecuencia = false;
            }
        }, velocidad);
    }

    // Resaltar una caja
    function resaltarCaja(caja) {
        caja.classList.add('activo');
        setTimeout(() => caja.classList.remove('activo'), velocidad / 2);
    }

    // Pasar a la siguiente ronda
    function siguienteRonda() {
        if (juegoTerminado) return;
        nivelActual++;
        secuenciaJugador = [];
        secuencia.push(obtenerCajaAleatoria());

        contadorRondaActual.textContent = "Ronda Actual: " + nivelActual;
        contadorRondas.textContent = "Rondas Completadas: " + (nivelActual - 1);

        mostrarSecuencia();
    }

    cajas.forEach(caja => {
        caja.addEventListener('click', () => {
            if (juegoTerminado || mostrandoSecuencia) return;

            secuenciaJugador.push(caja);
            const esCorrecto = secuenciaJugador.every((valor, i) => valor === secuencia[i]);

            if (!esCorrecto) {
                alert(`¡Incorrecto! Juego terminado. Tiempo total: ${tiempo} segundos`);
                juegoTerminado = true;
                clearInterval(intervaloTiempo); 

                // Enviar las estadísticas al servidor
                enviarEstadisticas(false);
            } else if (secuenciaJugador.length === secuencia.length) {
                setTimeout(siguienteRonda, 1000);
            }
        });
    });

    // Enviar las estadísticas cuando termine el juego
    function enviarEstadisticas(exito) {
        if (!nivelSeleccionado) {
            alert('Por favor, selecciona un nivel.');
            return;
        }

        const estadisticas = {
            id_intento: 1,
            id_usuario: idUsuario, 
            nivel: nivelSeleccionado, 
            rondas_completadas: nivelActual - 1,
            ronda_actual: nivelActual,
            tiempo_total: tiempo
        };

        console.log('Estadísticas a enviar:', estadisticas);

        fetch('http://localhost:5000/guardar_estadisticas_juegodecolor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(estadisticas)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Estadísticas guardadas:', data);
            alert(`Estadísticas guardadas correctamente.`);
        })
        .catch(error => {
            console.error('Error al guardar estadísticas:', error);
        });
    }
});


//**********************Juego03Trabalenguas******************/


document.addEventListener('DOMContentLoaded', function () {
    // Se crearn arrays con trabalenguas ya sean faciles , medios o difciles.
    var trabalenguasFaciles = [
        "Tres tristes tigres tragan trigo en un trigal.",
        "Pablito clavó un clavito. ¿Qué clavito clavó Pablito?",
        "Mi mamá me mima mucho.",
        "El perro de San Roque no tiene rabo porque Ramón Ramírez se lo ha cortado."
    ];
    var trabalenguasMedios = [
        "¿Cómo quieres que te quiera si el que quiero que me quiera no me quiere como quiero que me quiera?",
        "El cielo está encapotado, ¿quién lo desencapotará?",
        "A Cuesta le cuesta subir la cuesta, y en medio de la cuesta, Cuesta va y se acuesta.",
        "El hipopótamo hipo tiene hipo. ¿Quién le quita el hipo al hipopótamo hipo?"
    ];
    var trabalenguasDificiles = [
        "Pancha plancha con cuatro planchas, ¿con cuántas planchas plancha Pancha?",
        "El rey de Constantinopla está constantinoplizado.",
        "Juan tuvo un tubo, y el tubo que tuvo se le rompió. Y para recuperar el tubo, tuvo que comprar un tubo igual al tubo que tuvo",
        "Si Sansón no sazona su salsa con sal, le sale sosa. ¿Qué salsa sosa le sale a Sansón si no sazona su salsa con sal?"
    ];

    //Se declaran las variables.
    var contenedorTrabalenguas = document.getElementById('contenedor-trabalenguas');
    var botonEmpezar = document.getElementById('botonEmpezar');
    var botonParar = document.getElementById('botonTiempo');
    var tiempoP = document.getElementById('tiempo');
    var listaNiveles = document.getElementById('niveles');
    var nivelSeleccionado = "";
    var intervaloTiempo;
    var segundos = 0;

    // Mostrar niveles al hacer clic en el botón "Empezar"
    botonEmpezar.addEventListener('click', function () {
        listaNiveles.style.display = 'block';
    });

    // Seleccionar nivel y comenzar a contar el tiempo
    document.querySelectorAll('.nivel').forEach(function (boton) {
        boton.addEventListener('click', function (evento) {
            nivelSeleccionado = evento.target.dataset.nivel;
            listaNiveles.style.display = 'none';
            mostrarTrabalenguasAleatorio();
            iniciarTemporizador();  
        });
    });

    // Función para mostrar un trabalenguas
    function obtenerTrabalenguasPorNivel(nivel) {
        var lista = [];
        if (nivel === "facil") lista = trabalenguasFaciles;
        else if (nivel === "medio") lista = trabalenguasMedios;
        else if (nivel === "dificil") lista = trabalenguasDificiles;

        var indiceAleatorio = Math.floor(Math.random() * lista.length);
        return lista[indiceAleatorio];
    }

    // Mostrar el trabalenguas en el div
    function mostrarTrabalenguasAleatorio() {
        var trabalenguas = obtenerTrabalenguasPorNivel(nivelSeleccionado);
        contenedorTrabalenguas.innerHTML = `
            <div class="DivBoxTrabalenguas">
                <p>${trabalenguas}</p>
            </div>
        `;
    }

    // Función para iniciar el temporizador
    function iniciarTemporizador() {
        segundos = 0;  
        tiempoP.textContent = `Tiempo: ${segundos}`; 
        intervaloTiempo = setInterval(function () {
            segundos++;
            tiempoP.textContent = `Tiempo: ${segundos}`;  
        }, 1000); 
    }

    // Funcion para detener el temporizador.
    botonParar.addEventListener('click', function () {
        clearInterval(intervaloTiempo); 
        alert(`¡Tiempo detenido! Has tardado ${segundos} segundos en completar el trabalenguas.  Se enviaran las estadísticas.`);
        enviarDatosAJuegoTrabalenguas(idUsuario, nivelSeleccionado, segundos); 
    });

// Almacenar los datos del usuario (simulando un inicio de sesión)
const idUsuario = localStorage.getItem('id_usuario');; 

// Modificar la función para detener el temporizador y enviar datos
botonParar.addEventListener('click', function () {
    clearInterval(intervaloTiempo); 
    enviarDatosAJuegoTrabalenguas(idUsuario, nivelSeleccionado, segundos);
});

// Función para enviar datos al backend
function enviarDatosAJuegoTrabalenguas(idUsuario, nivel, tiempoTotal) {
    const datos = {
        id_usuario: idUsuario,
        nivel: nivel,
        tiempo_total: tiempoTotal
    };

    fetch('http://localhost:5000/guardar_estadisticas_trabalenguas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                console.error(`Error al guardar estadísticas: ${data.error}`);
            } else {
                console.log('Estadísticas guardadas correctamente:', data.message);
            }
        })
        .catch(error => console.error('Error al conectar con el backend:', error));
}


});






//*****************************JUEGO 01 CARTAS*****************************//
document.addEventListener('DOMContentLoaded', function () {
    const cartas = document.querySelectorAll('.DivGridJuego01 div');  
    const botonEmpezar = document.getElementById('botonEmpezar');
    const botonReiniciar = document.getElementById('botonReiniciar'); 
    const intentosRestantes = document.getElementById('intentosRestantes');
    const listaNiveles = document.getElementById('niveles');

    let nivelSeleccionado = "";
    let intentos = 0;
    let totalPares = 0;
    let cartasDestapadas = [];
    let paresEncontrados = 0;
    let bloqueado = false;

    // Un Array con las imágenes que tengo
    const imagenes = [
        'url(../images/Panda.png)', 'url(../images/Lobo.png)', 
        'url(../images/Gato.png)', 'url(../images/Elefante.png)', 
        'url(../images/Mono.png)'  
    ];

    let paresCartas = [];  

    // Mostrar niveles al hacer clic en el botón empezar
    botonEmpezar.addEventListener('click', function () {
        listaNiveles.style.display = 'block'; 
    });

    // Con este botón se reinicia el juego
    botonReiniciar.addEventListener('click', function () {
        window.location.reload();  
    });

    // Se selecciona el nivel
    document.querySelectorAll('.nivel').forEach(function (boton) {
        boton.addEventListener('click', function (evento) {
            nivelSeleccionado = evento.target.dataset.nivel;
            iniciarJuego();  
            listaNiveles.style.display = 'none'; 
        });
    });

    // Función para iniciar el juego
    function iniciarJuego() {
        if (nivelSeleccionado === "facil") {
            intentos = 4;
            totalPares = 2;
        } else if (nivelSeleccionado === "medio") {
            intentos = 5;
            totalPares = 3;
        } else if (nivelSeleccionado === "dificil") {
            intentos = 3;
            totalPares = 4;
        }

        intentosRestantes.textContent = `Intentos restantes: ${intentos}`;
        paresEncontrados = 0;

        cartasDestapadas = [];
        paresCartas = prepararPares();  
        desordenarCartas();  
        taparCartas();  
    }

    // Función para preparar las cartas pares
    function prepararPares() {
        let pares = [];
        for (let i = 0; i < imagenes.length - 1; i++) {
            pares.push(imagenes[i]);
            pares.push(imagenes[i]);  
        }
        pares.push(imagenes[imagenes.length - 1]);
        return pares;
    }

    // Función para mezclar las cartas
    function desordenarCartas() {
        paresCartas = paresCartas.sort(() => Math.random() - 0.5);  
    }

    // Función para tapar todas las cartas al iniciar
    function taparCartas() {
        cartas.forEach((carta, index) => {
            carta.style.backgroundImage = 'none'; 
            carta.style.backgroundColor = '#f39c12';  
            carta.dataset.imagen = paresCartas[index];  
            carta.addEventListener('click', voltearCarta);  
        });
    }

    // Función para voltear la carta cuando se hace clic
    function voltearCarta() {
        if (bloqueado || this.style.backgroundImage !== 'none') return;  
        this.style.backgroundImage = this.dataset.imagen;
        this.style.backgroundColor = ''; 
        cartasDestapadas.push(this); 
        if (cartasDestapadas.length === 2) {
            verificarPareja();  
        }
    }

    // Función para verificar si las dos cartas que se dan la vuelta son par
    function verificarPareja() {
        bloqueado = true;  
        const [carta1, carta2] = cartasDestapadas;

        if (carta1.dataset.imagen === carta2.dataset.imagen) {
            paresEncontrados++;  
            if (paresEncontrados === totalPares) {
                setTimeout(() => {
                    alert('¡Ganaste! Has encontrado todos los pares de cartas.');
                    enviarEstadisticas(true);
                }, 500);
            }
            resetearCartasDestapadas();  
        } else {
            intentos--;  
            intentosRestantes.textContent = `Intentos restantes: ${intentos}`;
            if (intentos === 0) {
                setTimeout(() => {
                    alert('¡Perdiste! Se acabaron los intentos.');
                    enviarEstadisticas(false);
                    bloquearCartas();
                }, 500);
            } else {
                setTimeout(() => {
                    carta1.style.backgroundImage = 'none';  
                    carta2.style.backgroundImage = 'none';  
                    carta1.style.backgroundColor = '#f39c12';
                    carta2.style.backgroundColor = '#f39c12';
                    resetearCartasDestapadas();
                }, 1000);
            }
        }
    }

    // Función para resetear las cartas dadas la vuelta
    function resetearCartasDestapadas() {
        cartasDestapadas = [];  
        bloqueado = false;  
    }

    // Bloquear las cartas después de perder
    function bloquearCartas() {
        cartas.forEach(carta => {
            carta.removeEventListener('click', voltearCarta);
        });
    }

    // Función para enviar las estadísticas al servidor
    function enviarEstadisticas(victoria) {
        const idUsuario = localStorage.getItem('id_usuario');
        const nivel = nivelSeleccionado;
        const intentosRealizados = 10 - intentos;  
        //Victoria o derrota depemdiendo de 0 o 1
        const derrota = victoria ? 0 : 1;  

        const datos = {
            id_usuario: idUsuario,
            nivel: nivel,
            intentos_realizados: intentosRealizados,
            victoria: victoria ? 1 : 0,
            derrota: derrota
        };

        fetch('http://localhost:5000/actualizar_estadisticas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            alert(data.message || 'Estadísticas guardadas correctamente.');
        })
        .catch(error => {
            console.error('Error al guardar estadísticas:', error);
        });
    }
});













//********************JUego04************* */

// Variables del juego
const cajas = document.querySelectorAll('.cajajuego');  
const botonEmpezar = document.getElementById('botonEmpezar'); 
const contadorDisplay = document.getElementById('contador'); 
const segundosDisplay = document.getElementById('segundos');
const botonContarIzquierda = document.getElementById('botonContarIzquierda');
const botonContarCentro = document.getElementById('botonContarCentro');
const botonContarDerecha = document.getElementById('botonContarDerecha');

let cartas = [];
let juegoActivo = false;
let contador = 0;
let segundos = 30;
let intervaloSegundos;
let cartasDisponibles = [];
let cajasOcupadas = []; 
let intervaloFrutas;

// Función que inicia o reinicia el juego
botonEmpezar.addEventListener('click', iniciarJuego);

// Asigna valores aleatorios a las cartas
function iniciarJuego() {
    if (juegoActivo) {
        reiniciarJuego();
    } else {
        juegoActivo = true;
        botonEmpezar.textContent = 'Reiniciar';
        contador = 0;
        contadorDisplay.textContent = contador;
        segundos = 30;
        segundosDisplay.textContent = segundos;

        const valores = [
            '🍌',  
            '🍊',  
            '🍉',  
            '🍎'   
        ]; 

        cartas = [...valores]; 
        cartas = cartas.concat(cartas);
        cartasDisponibles = [...cartas]; 

        // Se barajean las cartas con el random
        cartasDisponibles.sort(() => Math.random() - 0.5);
        // Se ponen las cajas centradas.
        cajas.forEach(caja => {
            caja.textContent = ''; 
            caja.style.pointerEvents = 'none'; 
            caja.style.textAlign = 'center';
            caja.style.display = 'flex'; 
            caja.style.justifyContent = 'center'; 
            caja.style.alignItems = 'center';
        });

        mostrarCartaAleatoria();

        // Temporizador
        intervaloSegundos = setInterval(() => {
            segundos--;
            segundosDisplay.textContent = segundos;
            if (segundos <= 0) {
                clearInterval(intervaloSegundos);
                clearInterval(intervaloFrutas); 
                alert('¡Se acabó el tiempo!'); 

                // Desactivar botones de contar
                botonContarIzquierda.disabled = true;
                botonContarCentro.disabled = true;
                botonContarDerecha.disabled = true;

                finalizarJuego();
            }
        }, 1000);
    }
}

// Mostrar las cartas de manera aleatoria en la cuadrícula
function mostrarCartaAleatoria() {
    if (segundos > 0) {
        intervaloFrutas = setInterval(() => {
            let cajaAleatoria = Math.floor(Math.random() * cajas.length); 

            // Asignamos el emoticono a la caja
            const carta = cartasDisponibles[Math.floor(Math.random() * cartasDisponibles.length)]; 
            cajas[cajaAleatoria].textContent = carta;

            // Se pone el emoticono con este tamaño 
            cajas[cajaAleatoria].style.fontSize = '60px'; 
            cajas[cajaAleatoria].style.pointerEvents = 'auto';

            setTimeout(() => {
                cajas[cajaAleatoria].textContent = ''; 
            }, 1000); 
        }, 1000); 
    }
}

// Contar las cartas en cada columna
// Como es una cuadrícula empieza en 0 a 8
botonContarIzquierda.addEventListener('click', () => contarCartasEnColumna([0, 3, 6], 'izquierda')); 
botonContarCentro.addEventListener('click', () => contarCartasEnColumna([1, 4, 7], 'centro')); 
botonContarDerecha.addEventListener('click', () => contarCartasEnColumna([2, 5, 8], 'derecha')); 

function contarCartasEnColumna(indices, columna) {
    let contadorColumna = 0;
    let contadorIncorrecto = 0; 

    // Contamos las cartas visibles en las casillas
    indices.forEach(index => {
        if (cajas[index].textContent !== '') { 
            // Comprobamos el tipo de carta y asignamos puntos
            const carta = cajas[index].textContent;
            if (carta === '🍌' || carta === '🍊') {
                contadorColumna += 1;
            } else if (carta === '🍉' || carta === '🍎') {
                contadorColumna += 2; 
            }
        }
    });

    // Si no hay emoticonos en la columna seleccionada se resta el contador
    if (contadorColumna === 0) {
        contadorIncorrecto = 1;
    }

    // Si la columna tiene carta suma el contador
    if (contadorIncorrecto === 0) {
        contador += contadorColumna;
    } else {
        contador -= 1; 
    }

    contadorDisplay.textContent = contador;
}

// Reiniciar el juego
function reiniciarJuego() {
    juegoActivo = false;
    botonEmpezar.textContent = 'Empezar';
    contador = 0;
    contadorDisplay.textContent = contador;
    segundos = 30;
    segundosDisplay.textContent = segundos;

    cajasOcupadas = []; 

    cajas.forEach(caja => {
        caja.textContent = '';
        caja.style.pointerEvents = 'none'; 
    });
    clearInterval(intervaloSegundos);
    clearInterval(intervaloFrutas); 

    // Reactivar los botones de contar
    botonContarIzquierda.disabled = false;
    botonContarCentro.disabled = false;
    botonContarDerecha.disabled = false;
}

// Función para finalizar el juego y enviar los datos
function finalizarJuego() {
    // Obtener el ID del usuario almacenado en el localStorage
    const idUsuario = localStorage.getItem('id_usuario');

    // Si el ID del usuario existe, proceder a enviar los datos
    if (idUsuario) {
        enviarDatosAJuegoConteoObjetos(idUsuario, contador);
    } else {
        console.log("No se ha encontrado el ID del usuario.");
    }
}

// Función para enviar los datos del juego al backend
function enviarDatosAJuegoConteoObjetos(idUsuario, contadorFinal) {
    const datos = {
        id_usuario: idUsuario,
        contador_final: contadorFinal
    };

    fetch('http://localhost:5000/guardar_estadisticas_conteoobjetos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                console.error(`Error al guardar estadísticas: ${data.error}`);
            } else {
                console.log('Estadísticas guardadas correctamente:', data.message);
            }
        })
        .catch(error => console.error('Error al conectar con el backend:', error));
}