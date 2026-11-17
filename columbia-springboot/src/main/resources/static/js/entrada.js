/* Pantalla de ingreso: animación de entrada (GSAP) y detalles del formulario. */
(function () {
    var form = document.querySelector('.cv-pasaje');
    var codigo = document.getElementById('codigo');
    var clave = document.getElementById('clave');
    var grupo = document.getElementById('campos');
    var boton = document.getElementById('embarcar');
    var ojo = document.getElementById('ver-clave');
    var demos = document.querySelectorAll('.cv-demo button');
    var reducido = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    /* Reloj del tablero */
    var reloj = document.getElementById('reloj');
    function tic() {
        var d = new Date();
        reloj.textContent = String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0');
    }
    tic();
    setInterval(tic, 15000);

    /* Entrada: un solo momento coreografiado, sólo al cargar la página */
    if (window.gsap) {
        if (window.MotionPathPlugin) gsap.registerPlugin(MotionPathPlugin);
        var mm = gsap.matchMedia();

        mm.add('(prefers-reduced-motion: no-preference)', function () {
            var tl = gsap.timeline({ defaults: { ease: 'expo.out', duration: 0.9 } });

            tl.from('.cv-terminal-marca', { autoAlpha: 0, y: -8, duration: 0.6 })
              .from('.cv-titular .cv-linea, .cv-bajada', {
                  autoAlpha: 0, y: 18, filter: 'blur(6px)', stagger: 0.09, clearProps: 'filter'
              }, '<0.1')
              .from('.cv-pasaje', { autoAlpha: 0, y: 24, scale: 0.98, duration: 1, clearProps: 'transform' }, '<0.15')
              .from('.cv-pasaje-cuerpo > *, .cv-talon', { autoAlpha: 0, y: 10, stagger: 0.05, duration: 0.7 }, '<0.2')
              .from('.cv-ruta-punteado', { autoAlpha: 0, duration: 0.6 }, '<')
              .from('.cv-ruta-punto, .cv-ruta-codigo', { autoAlpha: 0, scale: 0.4, transformOrigin: '50% 50%', stagger: 0.08, duration: 0.5 }, '<')
              .from('.cv-tablero', { autoAlpha: 0, y: 16 }, '<0.1')
              .from('.cv-tablero-fila', {
                  autoAlpha: 0, rotationX: -80, transformPerspective: 600, stagger: 0.07, duration: 0.6, ease: 'back.out(1.6)'
              }, '<0.2');

            if (window.MotionPathPlugin) {
                // El avión despega de Buenos Aires y llega al destino del próximo vuelo
                tl.fromTo('.cv-avion', { autoAlpha: 0 }, {
                    autoAlpha: 1, duration: 2.6, ease: 'power2.inOut',
                    motionPath: { path: '#cv-ruta-camino', align: '#cv-ruta-camino', alignOrigin: [0.5, 0.5], autoRotate: 90, end: 0.86 }
                }, 0.5);
            }
        });

        mm.add('(prefers-reduced-motion: reduce)', function () {
            if (window.MotionPathPlugin) {
                gsap.set('.cv-avion', { motionPath: { path: '#cv-ruta-camino', align: '#cv-ruta-camino', alignOrigin: [0.5, 0.5], autoRotate: 90, end: 0.86 } });
            }
        });
    }

    /* Error: sacudida + mensaje, que se va solo al escribir */
    function mostrarError(texto) {
        if (texto) document.querySelector('#error span').textContent = texto;
        grupo.classList.add('is-error');
        grupo.classList.remove('is-shaking');
        void grupo.offsetWidth;
        grupo.classList.add('is-shaking');
        setTimeout(function () { grupo.classList.remove('is-shaking'); }, 320);
    }
    function limpiarError() { grupo.classList.remove('is-error'); }
    codigo.addEventListener('input', limpiarError);
    clave.addEventListener('input', limpiarError);

    if (form.dataset.error === 'true') {
        // Error que vino del servidor: espera a que termine la entrada
        setTimeout(function () { mostrarError(); clave.focus(); }, reducido ? 0 : 900);
    } else if (!codigo.value) {
        codigo.focus({ preventScroll: true });
    }

    /* Mostrar u ocultar la clave */
    ojo.addEventListener('click', function () {
        var visible = clave.type === 'password';
        clave.type = visible ? 'text' : 'password';
        ojo.setAttribute('aria-pressed', String(visible));
        ojo.setAttribute('aria-label', visible ? 'Ocultar clave' : 'Mostrar clave');
    });

    /* Usuarios de prueba: completa los campos */
    demos.forEach(function (b) {
        b.addEventListener('click', function () {
            demos.forEach(function (o) { o.setAttribute('aria-pressed', 'false'); });
            b.setAttribute('aria-pressed', 'true');
            limpiarError();
            codigo.value = b.dataset.codigo;
            clave.value = '1234';
            if (window.gsap && !reducido) {
                gsap.fromTo([codigo, clave], { autoAlpha: 0.2, x: -6 }, { autoAlpha: 1, x: 0, duration: 0.35, ease: 'expo.out', stagger: 0.05 });
            }
            boton.focus({ preventScroll: true });
        });
    });

    /* Enviar: valida y muestra estado de carga */
    form.addEventListener('submit', function (e) {
        if (!codigo.value.trim() || !clave.value) {
            e.preventDefault();
            mostrarError('Completá el código y la clave.');
            (codigo.value.trim() ? clave : codigo).focus();
            return;
        }
        boton.classList.add('is-cargando');
        boton.setAttribute('aria-busy', 'true');
    });

    /* Si el navegador vuelve atrás con la página en caché, quitar el estado de carga */
    window.addEventListener('pageshow', function () {
        boton.classList.remove('is-cargando');
        boton.removeAttribute('aria-busy');
    });
})();
