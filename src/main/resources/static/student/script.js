document.addEventListener("DOMContentLoaded", function() {

    // Deletion Confirmation
    document.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete-btn')) {
            if (!confirm('Are you sure you want to permanently delete this student?')) {
                event.preventDefault();
            }
        }
    });

    // Logout Confirmation
    const logoutBtn = document.querySelector('.btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(event) {
            if (!confirm('Are you sure you want to log out?')) {
                event.preventDefault();
            }
        });
    }
});