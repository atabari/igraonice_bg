/**
 * Created by User on 3/15/2016.
 */

$('body').on('click', 'a[data-role="delete-paket"]', function (e) {
    e.preventDefault();
    $toDelete = $(this);
    swal({
        title: 'BRISANJE!!!',
        text: 'Da li sigurno želite obrisati paket?',
        type: 'warning',
        showCancelButton: true,
        showConfirmButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'DA, obrisi!',
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
                    text: 'Paket uspješno obrisan.',
                    type: 'success',
                    timer: 1000
                });
            });
        }
    });
});
