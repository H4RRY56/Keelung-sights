function loadSights(zone) {
    const sightList = document.getElementById("sight-list");

    sightList.innerHTML = `
        <div class="col-12 text-center">
            <p>載入中...</p>
        </div>
    `;

    fetch("/api/sights/" + zone)
        .then(response => {
            if (!response.ok) {
                throw new Error("HTTP error:" + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            sightList.innerHTML = "";

            data.forEach((sight, index) => {

                const mapQuery = "基隆市" + sight.sightName;

                sightList.innerHTML += `
                    <div class = "col-12 col-md-4 mb-4">
                        <div class="card h-100">
                            ${sight.photoURL
                    ? `<img src="${sight.photoURL}" class="card-img-top" alt="${sight.sightName}">`
                    : ""
                }
                    <div class="card-body">
                            <h5 class="card-title">${sight.sightName}</h5>
                            <p class="card-text">${sight.zone}</p>
                            ${sight.address && sight.address !== "-"
                    ? `
                                    <p class="card-text">
                                        <a href="https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(mapQuery)}"
                                           target="_blank">
                                            📍 ${sight.address}
                                        </a>
                                    </p>
                                  `
                    : `
                                    <p class="card-text">
                                        📍 無地址資訊
                                    </p>
                                  `
                }
                            <button class="btn btn-outline-primary"
                                    type="button"
                                    data-bs-toggle="collapse"
                                    data-bs-target="#detail-${index}">
                                    更多資訊
                            </button>
                            
                            <div class="collapse mt-3" id="detail-${index}">
                                <p>${sight.description}</p>
                            </div>
                            
                        </div>   
                    </div>
                </div>
                `;
            });
        })

        .catch(error => {
            console.error(error);

            sightList.innerHTML = `
            <div class="col-12 text-center">
                <p>資料載入失敗</p>
            </div>
        `
        })
}

