/**
 * Created by User on 2/3/2016.
 */
$('body').on('click', 'a[data-role="delete-user"]', function (e) {
    e.preventDefault();
    $toDelete = $(this);
    swal({
        title: 'BRISANJE!!!',
        text: 'Ukoliko izbrišete korisnika, automatski će se izbrisati sve njegove igraonice, paketi i rezervacije',
        type: 'warning',
        showCancelButton: true,
        showConfirmButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'DA, obriši!',
        cancelButtonText: 'NE, odustani!',
        confirmButtonClass: 'confirm-class',
        cancelButtonClass: 'cancel-class',
        showLoaderOnConfirm: true,
        closeOnConfirm: false,
        closeOnCancel: true
    }, function (isConfirm) {
        swal.disableButtons();
        if (isConfirm) {
            $.ajax({
                url: $toDelete.attr("href"),
                method: "delete"
            }).success(function (response) {
                $toDelete.parents($toDelete.attr("data-delete-parent")).remove();
                swal({
                    title: 'Obrisan!',
                    text: 'Korisnik uspješno obrisan.',
                    type: 'success',
                    timer: 1000
                });
            });
        }
    });
});









