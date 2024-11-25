// ---- Interacción con imágenes ----
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

document.addEventListener('DOMContentLoaded', function () {

    // Se crea la variable cajas y recorre todas las cajas con el id de boxSelectionJuego2
    const cajas = document.querySelectorAll('.DivBoxSectionJuego02');
    // Se crea la variable botonEmpezar 
    const botonEmpezar = document.getElementById('botonEmpezar');
    // Se crea la variable listaNiveles y busca el id niveles
    const listaNiveles = document.getElementById('niveles');
    
    // Variables para el contador.
    // Sirve para saber la ronda que está y cuántas completas tienes.
    const contadorRondas = document.getElementById('contadorRondas');
    const contadorRondaActual = document.getElementById('contadorRondaActual');

    // Variables declaradas.

    // Secuencia almacena los colores aleatoriamente.
    let secuencia = [];
    // y esta almacena la del jugador.
    let secuenciaJugador = [];
    // Sirve para saber la ronda que está el jugador.
    let nivelActual = 0;
    // Variable para velocidad.
    let velocidad = 1000;
    // Un boolean para saber si está juego funcionando o se termina.
    let juegoTerminado = false;  
    // Variable para controlar si la secuencia se está mostrando
    let mostrandoSecuencia = false;

    // Función click para que funcione el botón empezar.
    botonEmpezar.addEventListener('click', () => {
        listaNiveles.style.display = 'block';
    });

    // Se permite pulsar los botones de niveles "Fácil, Medio, Difícil"
    document.querySelectorAll('.nivel').forEach(boton => {
        boton.addEventListener('click', (evento) => {
            velocidad = parseInt(evento.target.dataset.velocidad);
            listaNiveles.style.display = 'none';
            iniciarJuego();
        });
    });

    // Se utiliza una función para iniciar el juego donde se resetean todas las variables para empezar de nuevo.
    function iniciarJuego() {
        secuencia = [];
        secuenciaJugador = [];
        nivelActual = 0;
        juegoTerminado = false;  

        // Se resetean las rondas.
        contadorRondas.textContent = "Rondas Completadas: 0";  
        contadorRondaActual.textContent = "Ronda Actual: 0";    
        siguienteRonda();
    }

    // Función para generar un número aleatorio para que sea aleatorio las cajas que se vayan a pintar.
    function obtenerCajaAleatoria() {
        const indiceAleatorio = Math.floor(Math.random() * cajas.length);
        return cajas[indiceAleatorio];
    }

    function mostrarSecuencia() {
        mostrandoSecuencia = true;  // Indica que estamos mostrando la secuencia
        let i = 0;
        
        const intervalo = setInterval(function() { 
            const caja = secuencia[i];
            resaltarCaja(caja);
    
            i++;
            if (i >= secuencia.length) {
                clearInterval(intervalo);
                mostrandoSecuencia = false;  // Termina de mostrar la secuencia
            }
        }, velocidad);
    }
    
    // Función que usa el CSS activo para poder cambiar el color de la caja.
    // En mi caso el CSS es de color background green.
    function resaltarCaja(caja) {
        caja.classList.add('activo');
        setTimeout(() => caja.classList.remove('activo'), velocidad / 2);
    }

    // Función para ir a la siguiente ronda.
    function siguienteRonda() {
        // Comprueba que el juego no ha terminado.
        if (juegoTerminado) return;  
        // Se suma el contador.
        nivelActual++;
        // Se reinicia el array
        secuenciaJugador = [];
        // Se utiliza para meter un nuevo elemento al array de la secuencia.
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
            if (juegoTerminado || mostrandoSecuencia) return;  // No permitir clics mientras se muestra la secuencia

            // Sirve para cuando pulsa el jugador esa caja se añade al array.
            secuenciaJugador.push(caja);
            
            // Se verifica si todos los elementos coinciden con la secuencia.
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



document.addEventListener('DOMContentLoaded', function () {
// Se crean arrays de tres niveles, que contienen cuatro trabalenguas de más fácil a más difícil.
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



    // Se buscan los id que pertenecen al html de la pagina del juego.
    var contenedorTrabalenguas = document.getElementById('contenedor-trabalenguas');
    var botonEmpezar = document.getElementById('botonEmpezar');
    var listaNiveles = document.getElementById('niveles');
    var nivelSeleccionado = "";

    // Mostrar niveles al hacer clic en el botón
    botonEmpezar.addEventListener('click', function () {
        listaNiveles.style.display = 'block';
    });

    //Se selecciona los botones de los niveles como el juego de colores.
    //Y añade un listener de click para poder pulsarlos.
    document.querySelectorAll('.nivel').forEach(function (boton) {
        boton.addEventListener('click', function (evento) {
            nivelSeleccionado = evento.target.dataset.nivel;
            listaNiveles.style.display = 'none';
            mostrarTrabalenguasAleatorio();
        });
    });

    // Se crea esta función para seleccionar el nivel y sacar uno aleatorio.
    function obtenerTrabalenguasPorNivel(nivel) {
        var lista = [];
        if (nivel === "facil") lista = trabalenguasFaciles;
        else if (nivel === "medio") lista = trabalenguasMedios;
        else if (nivel === "dificil") lista = trabalenguasDificiles;

        var indiceAleatorio = Math.floor(Math.random() * lista.length);
        return lista[indiceAleatorio];
    }

    // Mostrar un trabalenguas aleatorio en el div.
    function mostrarTrabalenguasAleatorio() {
        var trabalenguas = obtenerTrabalenguasPorNivel(nivelSeleccionado);

        contenedorTrabalenguas.innerHTML = `
            <div class="DivBoxTrabalenguas">
                <p>${trabalenguas}</p>
            </div>
        `;
    }
});

