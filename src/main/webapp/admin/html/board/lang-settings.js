/**
 * Created by Peter on 2016/10/23.
 */
// Dashboard settings
var settings = {
    preferredLanguage: "cn" // en/cn: Switch language to Chinese
};

var HA_I18N;

$.ajax({
    url: "i18n/" + settings.preferredLanguage + "/board.json",
    type: "GET",
    dataType: "json",
    success: function(data) {
        return HA_I18N = data;
    },
    async: false
});