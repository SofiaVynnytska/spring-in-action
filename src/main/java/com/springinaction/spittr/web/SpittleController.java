package com.springinaction.spittr.web;

import com.springinaction.spittr.Spittle;
import com.springinaction.spittr.data.DuplicateSpittleException;
import com.springinaction.spittr.data.SpittleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/spittles")
public class SpittleController {
    private final SpittleRepository spittleRepository;

    private static final String MAX_LONG_AS_STRING = "9223372036854775807";

    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String spittles(Model model) {
//        model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));
//        return "spittles";
//    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Spittle> spittles(@RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
                                  @RequestParam(value = "count", defaultValue = "20") int count) {
        return spittleRepository.findSpittles(max, count);
    }

//    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
//    public String spittle(@PathVariable long spittleId, Model model) {
//        model.addAttribute(spittleRepository.findOne(spittleId));
//        return "spittle";
//    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveSpittle(SpittleForm form, Model model) throws Exception {
        spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
                form.getLongitude(), form.getLatitude()));
        return "redirect:/spittles";
    }

    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
    public String spittle(@PathVariable("spittleId") long spittleId, Model model) {
        Spittle spittle = spittleRepository.findOne(spittleId);
        if (spittle == null) {
            throw new SpittleNotFoundException();
        }
        model.addAttribute(spittle);
        return "spittle";
    }

    @ExceptionHandler(DuplicateSpittleException.class)
    public String handleDuplicateSpittle() {
        return "error/duplicate";
    }
//    @RequestMapping(method= RequestMethod.GET)
//    public List<Spittle> spittles(
//            @RequestParam("max") long max,
//            @RequestParam("count") int count) {
//        return spittleRepository.findSpittles(max, count);
//    }
//    @RequestMapping(method = RequestMethod.GET)
//    public String spittles(Map model) {
//        model.put("spittleList", spittleRepository.findSpittles(Long.MAX_VALUE, 20));
//        return "spittles";
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public List<Spittle> spittles() {
//        return spittleRepository.findSpittles(Long.MAX_VALUE, 20);
//    }
}
