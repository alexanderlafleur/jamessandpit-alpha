function submitForm(action) {
    var e;

    e = document.getElementById('action');
    if (e == undefined){
        throw "No element with id of 'action'";
    }
    if (e.type != 'hidden'){
        throw "Element with id of 'action' is not a hidden field";
    }
    e.value = action;

    var form = document.getElementById('form');
    if (form == undefined){
        throw "No element with id of 'form'";
    }
    if (!(form.tagName.toLowerCase() == "form")){
        throw "Element with id of 'form' id not a form element";
    }

    form.submit();
}
