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
})();
