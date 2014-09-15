package fpt.qa.rubyweb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fpt.ruby.crawler.CrawlPhimChieuRap;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.service.mongo.MovieTicketService;
import com.mongodb.BasicDBObject;

@Controller
@RequestMapping("/admin")
public class AdminCotroller {
	private MovieTicketService movieTicketService;
	
	@PostConstruct
	public void init(){
		this.movieTicketService = new MovieTicketService();
	}
	
	@RequestMapping(value="/crawlPhimChieuRap", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BasicDBObject crawlPhimChieuRap(){
		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		try{
			crawlPhimChieuRap.crawlHaNoi();
			
		}
		catch (Exception ex){
			System.out.println("Done");
			return new BasicDBObject().append("status", "failed");
		}
		return new BasicDBObject().append("status", "success");
	}
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String testCombo(Model model){
		/*AbsoluteTime absoluteTime = new AbsoluteTime();
		TimeResult timeResult = absoluteTime.getAbsoluteTime("hôm nay là ngày gì");
		System.out.println(timeResult.getBeginTime());
		System.out.println(timeResult.getEndTime());*/
		return "admin";
	}
	
	private void saveSchedule(String cinName, String movieTitle, String time, int numdays){
		cinName = cinName.trim();movieTitle = movieTitle.trim(); time = time.trim();
		String[] times = time.split("\\s+");
		for (String etime : times){
			System.out.println(etime.trim().split(":")[0]);
			System.out.println(etime.trim().split(":")[1]);
			MovieTicket movieTicket = new MovieTicket();
			movieTicket.setCinema(cinName.trim());
			movieTicket.setMovie(movieTitle.trim());
			for (int i =0;i<numdays;i++){
				Date date = new Date();
				Calendar c = Calendar.getInstance(); 
				c.setTime(date); 
				c.add(Calendar.DATE, i);
				date = c.getTime();
				date.setHours(Integer.parseInt(etime.trim().split(":")[0]));
				date.setMinutes(Integer.parseInt(etime.trim().split(":")[1]));
				date.setSeconds(0);
				movieTicket.setDate(date);
				if (!cinName.isEmpty() && !movieTitle.isEmpty())
					movieTicketService.save(movieTicket);
			}
			
		}
	}
	
	@RequestMapping(value="/crawlManual", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
	public String crawlPhimChieuRap(@RequestParam("cin_name") String cinName, @RequestParam("mov_title") String movieTitle,
											@RequestParam("time") String time, @RequestParam("numdays") String numdays ,Model model){
		try{
			int numday = Integer.parseInt(numdays);
			saveSchedule(cinName, movieTitle, time, numday);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			model.addAttribute("status","failed");
			return "crawl";
			//return new BasicDBObject().append("status", "failed");
		}
		model.addAttribute("status","success");
		return "crawl";
		//return new BasicDBObject().append("status", "success");
	}
	
	@RequestMapping(value="/crawl", method = RequestMethod.GET)
	public String crawlManual(Model model){
		return "crawl";
	}
	
	
	@RequestMapping(value="/allTicket", method = RequestMethod.GET)
	@ResponseBody
	public String showTickets(){
		StringBuilder allTicketString = new StringBuilder();
		List<MovieTicket> movieTickets = movieTicketService.findAll();
		for (MovieTicket movieTicket : movieTickets){
			allTicketString.append(movieTicket.getDate().toGMTString() + " | " + movieTicket.getMovie() + " | " + movieTicket.getCinema());
			allTicketString.append("</br>");
		}
		return allTicketString.toString();
	}
	
	
	
}
