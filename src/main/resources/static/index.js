var Auth0Lock = require("auth0-lock");
var Reqwest = require("reqwest");
var jwt = require('jsonwebtoken');

(function (document, undefined) {

    var widget = undefined;

    var authTokenStore = {
        setToken: function (token) { localStorage.setItem("auth0token", token);},
        getToken: function () {return localStorage.getItem("auth0token");}
    };

    function setupPage() {

<<<<<<< Updated upstream
        var domain = 'FIXME.auth0.com';
=======
        var domain = 'FIXME';
>>>>>>> Stashed changes
        var cid = 'FIXME';

        widget = Auth0Lock(cid, domain);

        widget.show({
            focusInput: false,
            popup: true
        }, function (err, profile, token) {
            if (err) {
                console.error(err);
            }
            var btn = document.createElement("button");
            btn.setAttribute("id", "js-nickname");
            btn.textContent = profile['nickname'];
            btn.onclick = makeHandShake;
            document.body.appendChild(btn);
            userProfile = profile;
            console.log(token);
            authTokenStore.setToken(token)
        });

    }

    function makeHandShake(event) {
        var auth0token = authTokenStore.getToken();
        console.log(auth0token);
        widget.getProfile(auth0token, function(token, userinfo) {
            var textarea = document.createElement("textarea");
            textarea.setAttribute("cols", "15");
            textarea.setAttribute("rows", "15");
            textarea.textContent = JSON.stringify(userinfo, 0, 2);
            document.body.appendChild(textarea);
        });


        Reqwest({
            url: '/authorised/handshake'
            , type: 'html'
            , method: 'get'
            , contentType: 'text/plain'
            , headers: {
                'Authorization': 'Bearer ' + auth0token
            }
            , error: function (err) {
            }
            , success: function (resp) {
                console.log(resp.content);
            }
        })

    }

    setupPage();

})
(window.document, void 0);