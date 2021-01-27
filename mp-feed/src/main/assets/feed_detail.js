

    function showContent(content) {
        document.getElementById("main_content").innerHTML = content;
        switchMode()
        setFontSize()
//        setTextColor()
    }

    function setFontSize() {
      var fs =  getQueryVariable("fontSize")
      if (fs) {
         var obj = document.getElementById("main_content");
         obj.style.fontSize= fs;
      }
    }

//    function setTextColor() {
//        var textColor = getQueryVariable("textColor")
//
//        console.log(textColor)
//
//        if (textColor) {
//           var obj = document.getElementById("main_content");
//             obj.style.color= textColor;
//        }
//    }

    function switchMode() {
      var obj = document.getElementById("main_content");

      if (getQueryVariable("dark") == "true") {
          obj.setAttribute("class", "dark");
      } else {
          obj.setAttribute("class", "light");
      }
    }

    function getQueryVariable(variable) {

            var rsp = ""

           var query = window.location.search.substring(1);

           console.log(query)

           var vars = query.split("&");
           for (var i=0;i<vars.length;i++) {
                   var pair = vars[i].split("=");
                   if (pair[0] == variable) {
                       rsp = pair[1];
                       break;
                   }
           }

           var decodeRsp = decodeURI(rsp)
           return decodeRsp;
    }