package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll();
        //가방에 담기
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id) {
        return "board/updateForm";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO){
        System.out.println(requestDTO);

        //인증 체크
        if (requestDTO.getTitle().length() > 20 && requestDTO.getContent().length() > 20) {
            return "error/400";
        }

        //모델 위임
        boardRepository.save(requestDTO);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id){
        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id){

        boardRepository.deleteById(id);

        return "redirect:/";
    }
}
