/* =============================================
   MAIN.JS - Funcionalidades JavaScript
   Columbia Viajes
   ============================================= */

// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    
    // ===== INICIALIZACIÓN =====
    console.log('Columbia Viajes - Sistema cargado');
    
    // ===== TOOLTIPS DE BOOTSTRAP =====
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // ===== POPOVERS =====
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // ===== TOASTS (Notificaciones) =====
    var toastElList = [].slice.call(document.querySelectorAll('.toast'));
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl);
    });
    
    // Mostrar toasts automáticamente
    toastList.forEach(toast => toast.show());
    
    // ===== CONFIRMAR ELIMINACIONES =====
    const deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Está seguro que desea eliminar este elemento? Esta acción no se puede deshacer.')) {
                e.preventDefault();
            }
        });
    });
    
    // ===== VALIDACIÓN DE FORMULARIOS =====
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
    
    // ===== BÚSQUEDA EN TABLAS =====
    const searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(input => {
        input.addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const tableId = this.dataset.table;
            const table = document.getElementById(tableId);
            
            if (table) {
                const rows = table.querySelectorAll('tbody tr');
                rows.forEach(row => {
                    const text = row.textContent.toLowerCase();
                    row.style.display = text.includes(searchTerm) ? '' : 'none';
                });
            }
        });
    });
    
    // ===== ORDENAR TABLAS =====
    const sortableHeaders = document.querySelectorAll('.sortable');
    sortableHeaders.forEach(header => {
        header.addEventListener('click', function() {
            const table = this.closest('table');
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            const columnIndex = Array.from(this.parentNode.children).indexOf(this);
            const isAsc = this.classList.contains('asc');
            
            // Cambiar icono
            this.classList.toggle('asc', !isAsc);
            this.classList.toggle('desc', isAsc);
            
            // Quitar clases de otros headers
            this.parentNode.querySelectorAll('.sortable').forEach(h => {
                if (h !== this) {
                    h.classList.remove('asc', 'desc');
                }
            });
            
            // Ordenar filas
            rows.sort((a, b) => {
                const aText = a.children[columnIndex].textContent.trim();
                const bText = b.children[columnIndex].textContent.trim();
                
                // Intentar convertir a número
                const aNum = parseFloat(aText.replace(/[^\d.-]/g, ''));
                const bNum = parseFloat(bText.replace(/[^\d.-]/g, ''));
                
                if (!isNaN(aNum) && !isNaN(bNum)) {
                    return isAsc ? aNum - bNum : bNum - aNum;
                }
                
                // Orden alfabético
                return isAsc ? aText.localeCompare(bText) : bText.localeCompare(aText);
            });
            
            // Reinsertar filas ordenadas
            rows.forEach(row => tbody.appendChild(row));
        });
    });
    
    // ===== CONTADOR DE CARACTERES =====
    const textareas = document.querySelectorAll('textarea[data-maxlength]');
    textareas.forEach(textarea => {
        const maxLength = textarea.dataset.maxlength;
        const counterId = textarea.id + '-counter';
        let counter = document.getElementById(counterId);
        
        if (!counter) {
            counter = document.createElement('small');
            counter.id = counterId;
            counter.className = 'form-text text-muted';
            textarea.parentNode.appendChild(counter);
        }
        
        function updateCounter() {
            const currentLength = textarea.value.length;
            counter.textContent = `${currentLength}/${maxLength} caracteres`;
            
            if (currentLength > maxLength) {
                counter.classList.add('text-danger');
                textarea.classList.add('is-invalid');
            } else {
                counter.classList.remove('text-danger');
                textarea.classList.remove('is-invalid');
            }
        }
        
        textarea.addEventListener('input', updateCounter);
        updateCounter(); // Actualizar al cargar
    });
    
    // ===== SELECT2 PARA SELECTS MÚLTIPLES =====
    const advancedSelects = document.querySelectorAll('.select-advanced');
    advancedSelects.forEach(select => {
        // Simular funcionalidad de Select2
        select.addEventListener('focus', function() {
            this.style.background = '#fff';
            this.style.boxShadow = '0 0 0 0.2rem rgba(52, 152, 219, 0.25)';
        });
        
        select.addEventListener('blur', function() {
            this.style.background = '';
            this.style.boxShadow = '';
        });
    });
    
    // ===== MODAL DE CONFIRMACIÓN GENÉRICO =====
    window.showConfirmModal = function(title, message, callback) {
        const modal = document.getElementById('confirmModal');
        if (!modal) return;
        
        modal.querySelector('.modal-title').textContent = title;
        modal.querySelector('.modal-body').textContent = message;
        
        const confirmBtn = modal.querySelector('.btn-confirm');
        const oldOnClick = confirmBtn.onclick;
        confirmBtn.onclick = function() {
            if (callback) callback();
            bootstrap.Modal.getInstance(modal).hide();
        };
        
        new bootstrap.Modal(modal).show();
    };
    
    // ===== ACTUALIZAR HORA EN TIEMPO REAL =====
    function updateClock() {
        const clockElements = document.querySelectorAll('.live-clock');
        if (clockElements.length > 0) {
            const now = new Date();
            const timeString = now.toLocaleTimeString('es-AR', {
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            const dateString = now.toLocaleDateString('es-AR', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
            
            clockElements.forEach(el => {
                el.innerHTML = `<strong>${dateString}</strong> - ${timeString}`;
            });
        }
    }
    
    // Actualizar cada segundo
    setInterval(updateClock, 1000);
    updateClock(); // Primera llamada
    
    // ===== CARGAR DATOS VÍA AJAX (Ejemplo) =====
    window.loadData = function(url, targetElementId, callback) {
        const target = document.getElementById(targetElementId);
        if (!target) return;
        
        target.innerHTML = '<div class="text-center py-4"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Cargando...</span></div></div>';
        
        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (callback) {
                    callback(data, target);
                }
            })
            .catch(error => {
                console.error('Error al cargar datos:', error);
                target.innerHTML = '<div class="alert alert-danger">Error al cargar los datos</div>';
            });
    };
    
    // ===== EXPORTAR TABLAS A CSV =====
    window.exportToCSV = function(tableId, filename = 'export.csv') {
        const table = document.getElementById(tableId);
        if (!table) return;
        
        let csv = [];
        const rows = table.querySelectorAll('tr');
        
        rows.forEach(row => {
            const cols = row.querySelectorAll('td, th');
            const rowData = Array.from(cols).map(col => {
                let text = col.textContent.trim();
                // Escapar comillas
                text = text.replace(/"/g, '""');
                // Envolver en comillas si contiene coma
                if (text.includes(',')) {
                    text = '"' + text + '"';
                }
                return text;
            }).join(',');
            
            csv.push(rowData);
        });
        
        const csvString = csv.join('\n');
        const blob = new Blob([csvString], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        
        a.href = url;
        a.download = filename;
        a.click();
        
        window.URL.revokeObjectURL(url);
    };
    
    // ===== IMPRIMIR ELEMENTOS =====
    window.printElement = function(elementId) {
        const element = document.getElementById(elementId);
        if (!element) return;
        
        const printWindow = window.open('', '_blank');
        printWindow.document.write(`
            <html>
                <head>
                    <title>Imprimir - Columbia Viajes</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 20px; }
                        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
                        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                        th { background-color: #f2f2f2; }
                        .print-header { text-align: center; margin-bottom: 30px; }
                        .print-header h1 { color: #2c3e50; }
                        .print-footer { margin-top: 50px; text-align: center; font-size: 12px; color: #666; }
                        @media print {
                            .no-print { display: none !important; }
                        }
                    </style>
                </head>
                <body>
                    <div class="print-header">
                        <h1>Columbia Viajes</h1>
                        <p>Sistema de Gestión de Agencias</p>
                        <hr>
                    </div>
                    ${element.outerHTML}
                    <div class="print-footer">
                        <p>Documento generado el ${new Date().toLocaleDateString('es-AR')}</p>
                        <p>Sistema Columbia Viajes - www.columbiaviajes.com.ar</p>
                    </div>
                </body>
            </html>
        `);
        printWindow.document.close();
        printWindow.print();
    };
});

// ===== FUNCIONES GLOBALES =====

// Mostrar notificación toast
window.showToast = function(type, message) {
    const toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
        // Crear contenedor si no existe
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container position-fixed top-0 end-0 p-3';
        container.style.zIndex = '1050';
        document.body.appendChild(container);
    }
    
    const toastId = 'toast-' + Date.now();
    const toastHTML = `
        <div id="${toastId}" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header bg-${type} text-white">
                <strong class="me-auto">Notificación</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${message}
            </div>
        </div>
    `;
    
    document.getElementById('toast-container').innerHTML += toastHTML;
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
    
    // Remover toast después de que se esconda
    toastElement.addEventListener('hidden.bs.toast', function() {
        this.remove();
    });
};

// Validar email
window.isValidEmail = function(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
};

// Formatear moneda
window.formatCurrency = function(amount) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
    }).format(amount);
};

// Formatear fecha
window.formatDate = function(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-AR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};