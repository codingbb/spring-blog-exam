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
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        System.out.println("페이지 : " + page);
        List<Board> boardList = boardRepository.findAll(page);
        //가방에 담기
        request.setAttribute("boardList", boardList);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        boolean first = currentPage == 0 ? true : false;

        //lastpage는 db조회 필요 count
        int totalPage = boardRepository.count();
        int totalCount = totalPage / 5;
        boolean last = currentPage == totalCount ? true : false;

        request.setAttribute("first", first);
        request.setAttribute("last", last);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {

        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO){
        System.out.println(requestDTO);

        //유효성 검사 체크
        if (requestDTO.getTitle().length() > 20 || requestDTO.getContent().length() > 20) {
            return "error/400";
        }

        //모델 위임
        boardRepository.save(requestDTO);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){

        if (requestDTO.getTitle().length() > 20 || requestDTO.getContent().length() > 20) {
            return "error/400";
        }

        boardRepository.update(id, requestDTO);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id){

        boardRepository.deleteById(id);

        return "redirect:/";
    }
}
