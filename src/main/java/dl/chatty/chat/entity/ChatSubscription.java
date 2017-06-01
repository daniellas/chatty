package dl.chatty.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "id")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user", "chat", "sub" }))
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class ChatSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private Long chat;

    private String sub;
}
