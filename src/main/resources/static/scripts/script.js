let bearerToken;

function process(playlistID) {
    console.log('Processing..');
    let playlistData;
    getBearerToken().then(token => {
        if (token == null) {
            throw new Error('Bearer Token was not able to be retrieved. Please try again.');
        }
        else {
            console.log("Bearer Token: " + bearerToken);
            getPlaylistWithToken(playlistID).then(playlistData => {
                getPlaylistWithToken(playlistID).then(playlist => {
                    filterPlaylistData(playlistData);
                })
            })
        }
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

function sendPlaylistData(playlistData) {
    console.log("Sending playlist data to Youtube API...");
    fetch('/api/spotify', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
        // Add other headers if needed (e.g., authorization)
      },
      body: JSON.stringify(playlistData)
    });
}

function filterPlaylistData(playlistData) {
    console.log("Filtering playlist data...");

    const filteredData = {
        "Name": playlistData.name,
        "Tracks": {}
    }

    var tracks = []

    for (const track of playlistData.tracks) {
        tracks.push({
            key: track.name,
            value
        })
    }

    console.log(playlistData.name);
    console.log(playlistData.tracks);


}