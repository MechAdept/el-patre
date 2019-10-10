package com.samsolutions.controller.adminPanel;

import com.samsolutions.dto.data.TicketDataDTO;
import com.samsolutions.dto.form.TicketFormDTO;
import com.samsolutions.service.TicketService;
import com.samsolutions.service.UserService;
import com.samsolutions.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

/**
 * Controller of crud operations for table "ticket".
 *
 * @author Vladislav Brazovskij <u.brazouski@sam-solutions.com>
 * @package com.samsolutions.controller.adminPanel
 * @link http ://sam-solutions.com/
 * @copyright 2019 SaM
 */

@Controller
@RequestMapping("/adminpanel/ticket")
@PreAuthorize("isAuthenticated()")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserService userService;

    /**
     * Method to create a new ticket.
     *
     * @param ticketFormDTO form to create a ticket.
     * @return redirects to main page of "ticket" crud.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute(name = "ticketDTO") final TicketFormDTO ticketFormDTO, Model model) {
        ticketService.create(ticketFormDTO);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        model.addAttribute("userDataDto", userService.findById(ticketFormDTO.getPatientId()));
        return "/adminpanel/ticket/";
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String createForPatient(@PathVariable(value = "id", required = false) final Long id,
                                   Model model) {
        model.addAttribute("patient", userService.findById(id));
        model.addAttribute("ticketDTOForm", new TicketDataDTO());
        model.addAttribute("doctors", userService.findDoctors());
        return "/adminpanel/ticket/create";
    }

//    @RequestMapping(value = "/create/${id}", method = RequestMethod.GET)
//    public String createForDoctor(@PathVariable(value = "id", required = false) final Long id,
//                                   Model model) {
//        model.addAttribute("doctor", userService.findById(id));
//        model.addAttribute("healthDTOForm", new TicketDTO());
//        model.addAttribute(userService.findDoctors());
//        return "/adminpanel/health/create";
//    }

    @RequestMapping(method = RequestMethod.GET)
    public String read(final Model model,
                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                       @RequestParam(value = "desc", required = false, defaultValue = "true") Boolean desc,
                       @RequestParam(value = "sort", required = false, defaultValue = "datetime") String sort) {
        model.mergeAttributes(ticketService.getMapAndPage(pageNo, pageSize, desc, sort));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return "/adminpanel/user/n";
    }

    /**
     * Method to shows form for update record of ticket table.
     *
     * @param model is model.
     * @param id    is id.
     * @return return main page of "ticket" crud.
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        TicketDataDTO ticketDataDTO = ticketService.findById(id);
        model.addAttribute("ticketDTO", ticketDataDTO);
        model.addAttribute("ticketDTOForm", new TicketDataDTO());
        model.addAttribute("doctorsDTOList", userService.findDoctors());
        model.addAttribute("patientsDTOList", userService.findPatients());
        return "adminpanel/ticket/edit";
    }

    /**
     * Method for update record of "ticket" table.
     *
     * @param ticketFormDTO form to update a ticket.
     * @return redirects to main page of "ticket" crud.
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute final TicketFormDTO ticketFormDTO) {
        ticketService.create(ticketFormDTO);
        return "redirect: /adminpanel/ticket";
    }

    /**
     * Method to delete record from "ticket" table.
     *
     * @param id is id.
     * @return redirects to main page of "ticket" crud.
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") final Long id,
                         @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
                         @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                         @RequestParam(value = "sort", defaultValue = "datetime") String sort, Model model) {
        ticketService.delete(id);
        model.mergeAttributes(ticketService.getMapAndPage(pageNo, pageSize, desc, sort));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return "/adminpanel/ticket/crud";
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public String details(@PathVariable("id") final Long id, Model model) {
        model.addAttribute("ticketDTO", ticketService.findById(id));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return "/adminpanel/ticket/details/details";
    }

    @RequestMapping(value = "/details/{id}/visit", method = RequestMethod.GET)
    public String detailsVisit(@PathVariable("id") final Long id, Model model) {
        TicketDataDTO ticketDataDTO = ticketService.findById(id);
        model.addAttribute("ticketDTO", ticketDataDTO);
        model.addAttribute("visitDTO", visitService.findByTicketId(ticketDataDTO.getId()));
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return "/adminpanel/visit/details";
    }
}