package lk.ijse.dep9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueItem implements SuperEntity {
    private IssueItemPK issueItemPK;

    public IssueItem(int issueId, String isbn) {
        this.issueItemPK = new IssueItemPK(issueId, isbn);
    }
}
