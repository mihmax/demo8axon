package ua.dp.maxym.demo8.user.aggregate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record Email(@Id String email) {
}
