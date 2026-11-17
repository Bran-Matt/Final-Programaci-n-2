/* ===== Columbia Viajes - comportamiento de las pantallas ===== */
(function () {
    var reducido = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    // 1. Notificaciones: se muestran al cargar y se cierran solas
    document.querySelectorAll('.cv-toast').forEach(function (el) {
        var delay = parseInt(el.dataset.bsDelay || '4500', 10);
        var barra = el.querySelector('.cv-toast-barra');
        if (barra) barra.style.animationDuration = delay + 'ms';
        bootstrap.Toast.getOrCreateInstance(el).show();
    });

    // 2. Números que cuentan hacia arriba (data-contar="valor", data-prefijo="$ ")
    var formato = new Intl.NumberFormat('es-AR', { maximumFractionDigits: 0 });
    function contar(el) {
        var fin = parseFloat(el.dataset.contar) || 0;
        var prefijo = el.dataset.prefijo || '';
        if (reducido || fin === 0) { el.textContent = prefijo + formato.format(fin); return; }
        var duracion = 1100, inicio = null;
        function paso(t) {
            if (inicio === null) inicio = t;
            var p = Math.min((t - inicio) / duracion, 1);
            var suavizado = 1 - Math.pow(1 - p, 3); // arranca rápido y frena al final
            el.textContent = prefijo + formato.format(fin * suavizado);
            if (p < 1) requestAnimationFrame(paso);
        }
        requestAnimationFrame(paso);
    }
    document.querySelectorAll('[data-contar]').forEach(contar);

    // Las barras de progreso crecen desde cero
    document.querySelectorAll('.progress-bar[data-ancho]').forEach(function (barra) {
        requestAnimationFrame(function () {
            requestAnimationFrame(function () { barra.style.width = barra.dataset.ancho + '%'; });
        });
    });

    // 3. Tarjetas con role="button": también se abren con Enter o Espacio
    document.querySelectorAll('[role="button"][data-bs-toggle]').forEach(function (el) {
        el.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); el.click(); }
        });
    });

    // 4. Entrada de la página con GSAP: corta y sutil, porque se repite en cada pantalla
    if (window.gsap && !reducido) {
        var main = document.querySelector('main');
        var encabezado = main && main.querySelector('.cv-encabezado');
        if (encabezado) {
            var textos = encabezado.querySelectorAll('h1, p, .cv-icono, .btn');
            var bloques = Array.prototype.filter.call(
                main.querySelectorAll('.cv-encabezado ~ .row > [class*="col"], .cv-encabezado ~ .cv-card, .cv-encabezado ~ form, .col-lg-8 .d-grid > .cv-card'),
                function (el) { return el.getBoundingClientRect().top < window.innerHeight; }
            ).slice(0, 12);
            var tl = gsap.timeline({ defaults: { ease: 'expo.out' } });
            tl.from(textos, { autoAlpha: 0, y: 8, filter: 'blur(3px)', duration: 0.5, stagger: 0.04, clearProps: 'filter,transform,visibility,opacity' })
              .from(bloques, { autoAlpha: 0, y: 14, duration: 0.6, stagger: 0.04, clearProps: 'transform,visibility,opacity' }, '<0.08');
        }
    }
})();
