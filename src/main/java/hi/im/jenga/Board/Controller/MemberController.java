package hi.im.jenga.Board.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

    @RequestMapping(value = "/")
    public String hi(){

        return "main/main";
    }
    
    @RequestMapping(value = "/join")
    public String join(){

        return "member/join";
    }   
    
    @RequestMapping(value = "/setMemInfo")
    public String setMemberInfoPage(Model model){
    
    	
        return "member/setMemInfo";
    } 
    
    
    
    @RequestMapping(value = "/modMemInfo")
    public String modMemberInfoPage(Model model){
    	
    	model.addAttribute("user_nick", "김기덕");
    	model.addAttribute("user_email", "gdtbgl93@gmail.com");
    	String[] favors = {"영화", "IT"};
    	model.addAttribute("user_favor", favors);
    	
    	
        return "member/modMemInfo";
    } 
    
    
    
    @RequestMapping(value = "/updateMemInfo", method=RequestMethod.POST)
    public String updateMemberInfoAction(@RequestParam("favor") String[] favor){
    	
    	for(String str : favor)
    	System.out.println(str);

    
    	
    	
        return "member/modMemInfo";
    } 
    
    
	@ResponseBody
	@RequestMapping(value = "/getCategory", method = RequestMethod.GET)
	public List<Map<String, String>> getCategory() {

		List<Map<String, String>> params = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();

		map.put("name", "영화");
		map.put("image",
				"https://cdn20.patchcdn.com/users/22924509/20180619/041753/styles/T800x600/public/processed_images/jag_cz_movie_theater_retro_shutterstock_594132752-1529438777-6045.jpg");

		params.add(map);

		Map<String, String> map1 = new HashMap<String, String>();

		map1.put("name", "음악");
		map1.put("image", "https://lajoyalink.com/wp-content/uploads/2018/03/Movie.jpg");
		params.add(map1);

		Map<String, String> map2 = new HashMap<String, String>();

		map2.put("name", "미술");
		map2.put("image",
				"https://www.moma.org/d/assets/W1siZiIsIjIwMTUvMTAvMjEvaWJ3dmJmanIyX3N0YXJyeW5pZ2h0LmpwZyJdLFsicCIsImNvbnZlcnQiLCItcmVzaXplIDIwMDB4MjAwMFx1MDAzZSJdXQ/starrynight.jpg?sha=161d3d1fb5eb4b23");

		params.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();

		map3.put("name", "IT");
		map3.put("image", "https://www.indiewire.com/wp-content/uploads/2017/08/it-trailer-2-938x535.jpg?w=780");

		params.add(map3);

		Map<String, String> map4 = new HashMap<String, String>();

		map4.put("name", "시사");
		map4.put("image", "https://thumb.ad.co.kr/article/54/12/e8/92/i/459922.png");

		params.add(map4);

		return params;

	}
    
    
    
    
}
