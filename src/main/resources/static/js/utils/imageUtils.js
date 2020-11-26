function convertImageToDOM(image) {
    let imageDOM = document.createElement('img');
    imageDOM.className = 'image';
    if(window.location.href.includes('https'))
        if(!window.location.href.includes('https'))
            imageDOM.src = image.url.replace('http', 'https');
    else
        imageDOM.src = image.url;
    imageDOM.addEventListener('click', onClickImage);
    imageDOM.setAttribute('id', image.id);
    imageDOM.setAttribute('name', image.name);
    return imageDOM;
}

function onClickImage(e) {
    let targetSection = document.getElementById('target');
    targetSection.innerHTML = '';
    let imageDOM = e.toElement;
    if (imageDOM !== undefined) {
        let nameElement = document.createElement('span');
        let name = imageDOM.getAttribute('name')
        let url = imageDOM.getAttribute('src');
        nameElement.innerText = name;
        targetSection.appendChild(nameElement);
        targetSection.appendChild(convertImageToDOM({name:name,url:url,id:imageDOM.id}));
        let downloadBtn = document.getElementById('download-link');
        let file = name + url.substr(url.lastIndexOf('.'));
        downloadBtn.setAttribute('link', url);
        downloadBtn.setAttribute('file', file);
        currentPage.popUp.show();
    }
}