package com.columbiaviajesweb.columbia_springboot.controller;

import com.columbiaviajesweb.columbia_springboot.dto.RankingVendedor;
import com.columbiaviajesweb.columbia_springboot.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Sección del dueño: listado de vendedores ordenado por facturación.
 */
@Controller
@RequestMapping("/duenio")
public class DuenioController {

    private final UsuarioService usuarioService;

    public DuenioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        List<RankingVendedor> ranking = usuarioService.rankingVendedores();
        model.addAttribute("ranking", ranking);
        model.addAttribute("total", ranking.stream().mapToDouble(RankingVendedor::facturado).sum());
        model.addAttribute("reservas", ranking.stream().mapToInt(RankingVendedor::reservas).sum());
        return "duenio/ranking";
    }
}
