 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
      
   <footer class="footer footer-black footer-big align-bottom">
    <div class="container">
      <div class="row">
        <div class="col-md-2 col-sm-3 ml-auto mr-auto">
          <div class="logo text-center">
            <h2>Jenga</h2>
          </div>    
        </div>
        
         <div class="col-md-2 col-sm-2 ml-auto mr-auto text-center" style="margin : 10px">
		
		
		<div class="row">        
			<div class="col-12"><span>in Our Service...</span></div>
        
                  <div class="col-sm-12 col-md-6">
                       <h4>13.723
                      <small>peoples</small>
                    </h4>
                </div>
                <div  class="col-sm-12 col-md-6">
                    <h4>55.234
                    <small>blocks</small>
                    </h4>
                  </div>
          </div>
         </div>
        
        <div class="col-md-6 offset-md-1 col-sm-8 text-center">
          <div class="links">
            <ul>
              <li>
                <a href="#paper-kit">
                  Jenga Home
                </a>
              </li>
              <li>
                <a href="#paper-kit">
                  Jenga 소개
                </a>
              </li>
              <li>
                <a href="#paper-kit">
                  	이용약관
                </a>
              </li>
              <li>
                <a href="#paper-kit">
                  About us
                </a>
              </li>
              <li>
                <a href="#paper-kit">
                  Contact
                </a>
              </li>
            </ul>
            <hr />
            <div class="copyright">
              <div class="pull-left">
                ©
                <script>
                  document.write(new Date().getFullYear())
                </script> all copyrights reserved Jenga.
              </div>
              <div class="pull-right">
                <ul>
                  <li>
                    <a href="#paper-kit">
                      Terms
                    </a>
                  </li>               
                  <li>
                    <a href="#paper-kit">
                      Privacy
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </footer>


 <script>

     let footerObj = new Footer();

   function Footer(){

     let $footer;

     (function(){

      $footer = $("footer");

     })();

     this.getFooterHeight = function(){

       let footerHeight = $footer.css("height").replace(REGEX_TRIM_DIM_SIZE_EXTEND, "");
       return parseInt(footerHeight, 10);


     }

   }


   function setWindowSize($element){


       let documentHeight = $(document).height();
       let footerObj = new Footer();
       let bodySize = documentHeight - footerObj.getFooterHeight() - navbarObj.getNavbarHeight();
       $element.css("min-height", bodySize);


   }

   function setAutomaticResizeWindow($element){

       setWindowSize($element);
       $(window).on("resize", function(){
           setWindowSize($element);


       });


   }






 </script>