package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.entity.Inbox;
import java.util.List;

/**
 *
 * @author tjdckscert
 */
public class MailPageing {
	// 전체 글의 행의 수
	private int total;
	// 현재 페이지 번호
	private int currentPage;
	// 전체 페이지 개수
	private int totalPages;
	// 시작 페이지 번호
	private int startPage;
	// 종료 페이지 번호
	private int endPage;
	// 페이징의 개수
	private int pagingCount;
	// 게시글 데이터
	private List<Inbox> content;
	
	public MailPageing(int total, int currentPage, int size, int pagingCount, List<Inbox> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		this.pagingCount = pagingCount;		
		if(total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else { 
			totalPages = total / size;
			if(total % size > 0) {
				totalPages++;
			}
			startPage = currentPage / pagingCount * pagingCount + 1;
			if(currentPage % pagingCount == 0) {
				startPage -= pagingCount;
			}
			
			endPage = startPage + pagingCount - 1 ;
			if(endPage > totalPages) {
				endPage = totalPages;
			}
		}
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public boolean hasNoArticles() {
		return this.total == 0;
	}
	
	public boolean hasArticles() {
		return this.total > 0;
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public List<Inbox> getContent(){
		return this.content;
	}
	
	public int getStartPage() {
		return this.startPage;
	}
	
	public int getEndPage() {
		return this.endPage;
	}
	
}
