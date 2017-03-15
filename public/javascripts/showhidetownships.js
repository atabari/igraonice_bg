/**
 * Created by User on 2/2/2016.
 */
$('#ddlocation').change(function(){
    if( $(this).val() == 'Sarajevo'){
        $('#opstine').show();
    }else
    if( $(this).val() != '1'){
        $('#opstine').hide();
    }
});
