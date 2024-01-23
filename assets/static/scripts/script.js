let bearerToken;

function process(playlistID) {
    console.log('Processing..');

    if (playlistID.length != 22) {
        return;
    }

    getBearerToken()
        .then(token => {
            if (token == null) {
                throw new Error('Bearer Token was not able to be retrieved. Please try again.');
            }
            return getPlaylistWithToken(playlistID);
        })
        .then(playlistData => {
            return filterPlaylistData(playlistData);
        })
        .then(filteredData => {
            sendPlaylistData(filteredData);
        })
        .catch(error => {
            console.error('Error:', error);
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
    body: JSON.stringify(playlistData),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    // Handle successful response if needed
    console.log('Playlist data sent successfully');
  })
  .catch(error => {
    console.error('Error:', error);
  });
}


function filterPlaylistData(playlistData) {
    console.log("Filtering playlist data...");

    const filteredData = [
    ]

    filteredData[0] = playlistData.name,

    console.log(playlistData.name);

    playlistName = playlistData.name;

    document.getElementById("playlistName").innerHTML = "Playlist Name: " + playlistName;

    if (playlistName != null) {
        var elementToHide = document.getElementById('content');
        elementToHide.style.display = 'none';

        var name = document.getElementById('playlistName');
        var liveUpdates = document.getElementById('liveUpdates');

    }

    var tracks = [];

    for (const track of playlistData.tracks.items) {
        tracks.push({
            key: track.track.name,
            value: track.track.artists[0].name
        })
    }

    filteredData[1] = tracks;

    return filteredData;

}