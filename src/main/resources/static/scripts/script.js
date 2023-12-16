function getBearerToken() {
    console.log("Getting bearer token..")
    return fetch('/api/get/bearerToken')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            return data; // Assuming data is the bearer token
        });
}

function getPlaylistWithToken(playlistID) {
    getBearerToken()
        .then(accessToken => {
            // Now you have the access token, you can make the playlist request
            return fetch("https://api.spotify.com/v1/playlists/" + playlistID, {
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log(response);
            return response.json();
        })
        .then(data => {
            // Update the HTML content with the API response
            document.getElementById('result').innerText = JSON.stringify(data);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}