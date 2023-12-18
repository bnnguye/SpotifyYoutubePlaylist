let bearerToken;

function process(playlistID) {
    console.log('Processing..');
    getBearerToken().then(token => {
        if (token == null) {
            throw new Error('Bearer Token was not able to be retrieved. Please try again.');
        }
        console.log("Bearer Token: " + bearerToken);
        let playlistData = getPlaylistWithToken(playlistID);
        console.log(playlistData);
    });
}

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
            bearerToken = data;
            return data;
        });
}

function getPlaylistWithToken(playlistID) {
    if (bearerToken == null) { throw new Error('Bearer Token is null.')};
    return fetch("https://api.spotify.com/v1/playlists/" + playlistID, {
        headers: {
            'Authorization': 'Bearer ' + bearerToken
        }
    }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}