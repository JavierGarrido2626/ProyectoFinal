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
        window.location.href = "pages/Juego01.html";
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
        window.location.href = "pages/Juego02.html";
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
        window.location.href = "pages/Juego03.html";
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
        window.location.href = "pages/Juego04.html";
    });
});




//***************************JUEGO 02***************************//

document.addEventListener('DOMContentLoaded', function () {

    //Se crea las Costantes para el juego como las cajsa el boto y los contadores.
    const cajas = document.querySelectorAll('.DivBoxSectionJuego02');
    const botonEmpezar = document.getElementById('botonEmpezar');
    const listaNiveles = document.getElementById('niveles');
    const contadorRondas = document.getElementById('contadorRondas');
    const contadorRondaActual = document.getElementById('contadorRondaActual');


    // Se crean las variables 
    let secuencia = [];
    let secuenciaJugador = [];
    let nivelActual = 0;
    let velocidad = 1000;
    let juegoTerminado = false;  
    let mostrandoSecuencia = false;


    // Función click para que funcione el botón empezar.
    botonEmpezar.addEventListener('click', () => {
        listaNiveles.style.display = 'block';
    });

    // Se permite pulsar los botones de niveles.
    document.querySelectorAll('.nivel').forEach(boton => {
        boton.addEventListener('click', (evento) => {
            velocidad = parseInt(evento.target.dataset.velocidad);
            listaNiveles.style.display = 'none';
            iniciarJuego();
        });
    });

    //Se resetean las variables.
    function iniciarJuego() {
        secuencia = [];
        secuenciaJugador = [];
        nivelActual = 0;
        juegoTerminado = false;  
        contadorRondas.textContent = "Rondas Completadas: 0";  
        contadorRondaActual.textContent = "Ronda Actual: 0";    
        siguienteRonda();
    }
        //Creo esta funcion para poner un numero aleatorio y se recorra para que se coloree la cajas aleatoriomente.
        function obtenerCajaAleatoria() {
            const indiceAleatorio = Math.floor(Math.random() * cajas.length);
            return cajas[indiceAleatorio];
    }

    //Funcion para mostrar la secuencia 
    function mostrarSecuencia() {
        mostrandoSecuencia = true;  
        let i = 0;
        
        const intervalo = setInterval(function() { 
            const caja = secuencia[i];
            resaltarCaja(caja);
    
            i++;
            if (i >= secuencia.length) {
                clearInterval(intervalo);
                mostrandoSecuencia = false;  
            }
        }, velocidad);
    }
    
    //Esta funcion sirve para colorear la caja y usar el color verde en css.
    function resaltarCaja(caja) {
        caja.classList.add('activo');
        setTimeout(() => caja.classList.remove('activo'), velocidad / 2);
    }

    // Funcion para pasar la siguiente ronda
    function siguienteRonda() {
        // Comprueba que el juego no ha terminado.
        if (juegoTerminado) return;  
        // Se suma el contador.
        nivelActual++;
        secuenciaJugador = [];
        secuencia.push(obtenerCajaAleatoria());

        // Actualiza los contadores
        contadorRondaActual.textContent = "Ronda Actual: " + nivelActual;
        contadorRondas.textContent = "Rondas Completadas: " + (nivelActual - 1);

        mostrarSecuencia();
    }

    // Verifica la secuencia del jugador mediante la función click
    cajas.forEach(caja => {
        caja.addEventListener('click', () => {
            // Si termina el juego ya no puede dar más click
            if (juegoTerminado || mostrandoSecuencia) return;  
            secuenciaJugador.push(caja);
            const esCorrecto = secuenciaJugador.every((valor, i) => valor === secuencia[i]);
            // Comprueba si la caja que se ha hecho click ha sido de la secuencia y si es correcta sigue y si no sale un mensaje.
            if (!esCorrecto) {
                alert('¡Incorrecto! Juego terminado.');
                juegoTerminado = true;  
            } else if (secuenciaJugador.length === secuencia.length) {
                setTimeout(siguienteRonda, 1000);
            }
        });
    });
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

    // Mostrar el trabalenguas en el contenedor
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
    });
});





//*****************************JUEGO 01 CARTAS*****************************//

//Se declaran las variables.
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

    // Un Array con las imagens que tengo
    const imagenes = [
        'url(../images/Panda.png)', 'url(../images/Lobo.png)', 
        'url(../images/Gato.png)', 'url(../images/Elefante.png)', 
        'url(../images/Mono.png)'  
    ];

    let paresCartas = [];  

    // Mostrar niveles al hacer clic en el boton empezar
    botonEmpezar.addEventListener('click', function () {
        listaNiveles.style.display = 'block'; 
    });
    //Con este boton se reinciie el juego
    botonReiniciar.addEventListener('click', function () {
        window.location.reload();  
    });
    //Se selecciona el nivel 
    document.querySelectorAll('.nivel').forEach(function (boton) {
        boton.addEventListener('click', function (evento) {
            nivelSeleccionado = evento.target.dataset.nivel;
            iniciarJuego();  
            listaNiveles.style.display = 'none'; 
        });
    });

    // Función para iniciar el juego
    function iniciarJuego() {
      //Else if para seleccionar el juego con sus intentos 
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
            //Se añaden dos para que sean pares.
            pares.push(imagenes[i]);
            pares.push(imagenes[i]);  
        }

        pares.push(imagenes[imagenes.length - 1]);

        return pares;
    }

    // Función para mezclar las cartas
    function desordenarCartas() {
        //Se usa un numero ramdom para ello
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
        //Verifica si es par
        if (cartasDestapadas.length === 2) {
            verificarPareja();  
        }
    }

    // Función para verificar si las dos cartas que se dan la vuelta son par.
    function verificarPareja() {
        //Bloque el click
        bloqueado = true;  
        const [carta1, carta2] = cartasDestapadas;

        if (carta1.dataset.imagen === carta2.dataset.imagen) {
            paresEncontrados++;  
            if (paresEncontrados === totalPares) {
                setTimeout(() => alert('¡Ganaste! Has encontrado todos los pares de cartas.'), 500);
            }
            resetearCartasDestapadas();  
        } else {
            //Se resta el intento
            intentos--;  
            intentosRestantes.textContent = `Intentos restantes: ${intentos}`;
            if (intentos === 0) {
                setTimeout(() => alert('¡Perdiste! Se acabaron los intentos.'), 500);
                bloquearCartas();
            } else {
                // Volver a tapar las cartas si no son un pares
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

    // Función para resetear las cartas dadas las vuelta
    function resetearCartasDestapadas() {
        cartasDestapadas = [];  
        bloqueado = false;  
    }

    // Bloquear las cartas despues de perder
    function bloquearCartas() {
        cartas.forEach(carta => {
            carta.removeEventListener('click', voltearCarta);
        });
    }
});




/****Funcion para modo relajante */

document.addEventListener('DOMContentLoaded', function () {
    //Se crean las variabels y se busca su id
    const botonOlas = document.querySelector('.boton-sonido-olas');
    const botonBosque = document.querySelector('.boton-sonido-bosque');
    const botonLluvia = document.querySelector('.boton-sonido-lluvia');

    // Se buscan los sonidos
    const sonidoOlas = new Audio('../sounds/olas.mp3');
    const sonidoBosque = new Audio('../sounds/bosque.mp3');
    const sonidoLluvia = new Audio('../sounds/lluvia.mp3');
    //Que sehaga un bucle con los sonidos
    sonidoOlas.loop = true;
    sonidoBosque.loop = true;
    sonidoLluvia.loop = true;


    //Se declaran variable.s
    let sonidoActivo = null;
    let sonidoGuardado = localStorage.getItem('sonidoActual');
    let tiempoGuardado = localStorage.getItem('tiempoSonidoActual');

        //Se verifica si sigue la cancion sonadno
    if (sonidoGuardado) {
        if (sonidoGuardado === 'olas') {
            sonidoOlas.currentTime = tiempoGuardado ? parseFloat(tiempoGuardado) : 0;
            sonidoOlas.play().catch(error => console.error("Error al reproducir el sonido de olas:", error));
            sonidoActivo = sonidoOlas;
        } else if (sonidoGuardado === 'bosque') {
            sonidoBosque.currentTime = tiempoGuardado ? parseFloat(tiempoGuardado) : 0;
            sonidoBosque.play().catch(error => console.error("Error al reproducir el sonido de bosque:", error));
            sonidoActivo = sonidoBosque;
        } else if (sonidoGuardado === 'lluvia') {
            sonidoLluvia.currentTime = tiempoGuardado ? parseFloat(tiempoGuardado) : 0;
            sonidoLluvia.play().catch(error => console.error("Error al reproducir el sonido de lluvia:", error));
            sonidoActivo = sonidoLluvia;
        }
    }

    // Función para pausar todos la musica
    function pausarTodosLosSonidos() {
        sonidoOlas.pause();
        sonidoBosque.pause();
        sonidoLluvia.pause();
    }

    // Función para guardar el sonido
    function guardarSonido(sonido, tiempo) {
        localStorage.setItem('sonidoActual', sonido);
        localStorage.setItem('tiempoSonidoActual', tiempo);
    }
    //Listener para los botones
    botonOlas.addEventListener('click', function () {
        if (sonidoActivo === sonidoOlas) {
            sonidoOlas.pause();
            sonidoActivo = null;
            localStorage.removeItem('sonidoActual');
        } else {
            pausarTodosLosSonidos();
            guardarSonido('olas', 0);
            sonidoOlas.play().catch(error => console.error("Error al reproducir el sonido de olas:", error));
            sonidoActivo = sonidoOlas;
        }
    });

    botonBosque.addEventListener('click', function () {
        if (sonidoActivo === sonidoBosque) {
            sonidoBosque.pause();
            sonidoActivo = null;
            localStorage.removeItem('sonidoActual');
        } else {
            pausarTodosLosSonidos();
            guardarSonido('bosque', 0);
            sonidoBosque.play().catch(error => console.error("Error al reproducir el sonido de bosque:", error));
            sonidoActivo = sonidoBosque;
        }
    });

    botonLluvia.addEventListener('click', function () {
        if (sonidoActivo === sonidoLluvia) {
            sonidoLluvia.pause();
            sonidoActivo = null;
            localStorage.removeItem('sonidoActual');
        } else {
            pausarTodosLosSonidos();
            guardarSonido('lluvia', 0);
            sonidoLluvia.play().catch(error => console.error("Error al reproducir el sonido de lluvia:", error));
            sonidoActivo = sonidoLluvia;
        }
    });

    // Guardar el tiempo actual del sonido cuando el usuario sale de la pagina
    window.addEventListener('beforeunload', function () {
        if (sonidoActivo) {
            guardarSonido(
                sonidoActivo === sonidoOlas ? 'olas' :
                sonidoActivo === sonidoBosque ? 'bosque' :
                'lluvia', 
                sonidoActivo.currentTime
            );
        }
    });
}





);



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

// Función que inicia el juego
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

        // Se usan emoticonos
        const valores = [
            '🍌',  // Platano
            '🍊',  // Naranja
            '🍉',  // Sandia
            '🍎'   // Manzana
        ]; 


        cartas = [...valores]; 
        cartas = cartas.concat(cartas);
        cartasDisponibles = [...cartas]; 

        // Se barejean las cartas con el random
        cartasDisponibles.sort(() => Math.random() - 0.5);
        //Se pone las cajas centradas.
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
                alert('¡Se acabó el tiempo!');
                reiniciarJuego();
            }
        }, 1000);
    }
}

// Mostrar las cartas de manera aleatoria en la cuadrícula
function mostrarCartaAleatoria() {
    if (cartasDisponibles.length > 0) {
        let cajaAleatoria;
        do {
            cajaAleatoria = Math.floor(Math.random() * cajas.length); 
        } while (cajasOcupadas.includes(cajaAleatoria)); 

        // Marcamos la caja como ocupada
        cajasOcupadas.push(cajaAleatoria);

        // Asignamos el emoticono a la caja
        const carta = cartasDisponibles.pop(); 
        cajas[cajaAleatoria].textContent = carta;

        // Se pone el emoticono con este tamaño 
        cajas[cajaAleatoria].style.fontSize = '60px'; 
        cajas[cajaAleatoria].style.pointerEvents = 'auto';

        // Esperamos 2 segundos y luego quitamos la carta
        setTimeout(() => {
            cajas[cajaAleatoria].textContent = ''; 
            if (cajasOcupadas.length < cajas.length) {
                mostrarCartaAleatoria(); 
            }
        }, 2000); 
    }
}

// Contar las cartas en cada columna
//Como es una cuadricula empieza en 0 a 8
botonContarIzquierda.addEventListener('click', () => contarCartasEnColumna([0, 3, 6], 'izquierda')); 
botonContarCentro.addEventListener('click', () => contarCartasEnColumna([1, 4, 7], 'centro')); 
botonContarDerecha.addEventListener('click', () => contarCartasEnColumna([2, 5, 8], 'derecha')); 

function contarCartasEnColumna(indices, columna) {
    let contadorColumna = 0;
    let contadorIncorrecto = 0; 

    // Contamos las cartas visibles en las casillas
    indices.forEach(index => {
        if (cajas[index].textContent !== '') { 
            contadorColumna++;
        }
    });

    // Si no hay emoticonos en la columna seleccionada se resta el contador
    if (contadorColumna === 0) {
        contadorIncorrecto = 1;
    }

    // Si la columna tiene carta suma el ocntador
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
}
