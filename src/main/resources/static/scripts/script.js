function process(playlistID) {

    getPlaylist(playlistID);
}

function

function getBearerToken() {
    console.log("Getting bearer token..")
    var client_id =
    var client_secret = '';
    var authOptions = {
      url: 'https://accounts.spotify.com/api/token',
      headers: {
        'Authorization': 'Basic ' + (new Buffer.from(client_id + ':' + client_secret).toString('base64'))
      },
      form: {
        grant_type: 'client_credentials'
      },
      json: true
    };
    request.post(authOptions, function(error, response, body) {
      if (!error && response.statusCode === 200) {
        var token = body.access_token;
      }
    });
}

function getPlaylist(playlistID) {
    fetch("https://api.spotify.com/v1/playlists/" + playlistID)
                .then(response => response.json())
                .then(data => {
                    // Update the HTML content with the API response
                    document.getElementById('result').innerText = JSON.stringify(data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
    var result = document.getElementById('result');
    console.log(result);
}