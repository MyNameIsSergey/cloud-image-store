
currentPage = {

    init: function () {
        hideBanner();
        hideProgress();
        document.getElementById('upload-button').onclick = (e) => {
            upload(document.getElementById('file'), document.getElementById('name'));
        };
        document.getElementById('file').addEventListener('change', onFileChanged);
    },
    clone: function () {
        return { init: this.init }
    }
}

function onFileChanged(e) {
    let reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById('image-preview').src = e.target.result;
    };
    reader.readAsDataURL(e.currentTarget.files[0]);

}

function upload(file, name) {
    if(file.files.length === 0)
        return;
    let formData = new FormData();
    formData.append('file', file.files[0]);
    formData.append('name', name.value);
    $.ajax({
        url: 'api/images/',
        data: formData,
        enctype: "multipart/form-file",
        cache: false,
        processData: false,
        contentType: false,
        type: 'POST',
        beforeSend: function(XMLHttpRequest) {
            setProgress(0);
            showProgress();
        },
        xhr: function() {
            let xhr = new window.XMLHttpRequest();

            xhr.upload.onprogress = function(evt) {
                if (evt.lengthComputable) {
                    let percentComplete = evt.loaded / evt.total;
                    percentComplete *= 100;
                    setProgress(percentComplete);
                }
            };
            return xhr;
        },
        success: function(data){
            hideProgress();
        },
        headers: getHeaders()
    });
}

function setProgress(value) {
    document.getElementById('progressBar').style.width = value + '%';
    document.getElementById('progressBarText').innerHTML = value + '%';
}

function hideProgress() {
    document.getElementById('progressBar').classList.add('hide');
}

function showProgress() {
    document.getElementById('progressBar').classList.remove('hide');
}
