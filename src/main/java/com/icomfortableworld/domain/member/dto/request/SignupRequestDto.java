package com.icomfortableworld.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {
	@NotBlank(message = "username은 공백일 수 없습니다.")
	@Pattern(regexp = "^(?=.*[a-z]).+$", message = "username은 알파벳 소문자가 포함되어야 합니다.")
	@Pattern(regexp = "^(?=.*\\d).+$", message = "username 숫자가 포함되어야 합니다.")
	@Size(min = 4, max = 10, message = "username 4자에서 10자여야 합니다.")
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String nickname;
	@NotBlank(message = "password는 공백일 수 없습니다.")
	@Pattern(regexp = "^(?=.*[a-z]).+$", message = "비밀번호는 알파벳 소문자가 포함되어야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Z]).+$", message = "비밀번호는 알파벳 대문자가 포함되어야 합니다.")
	@Pattern(regexp = "^(?=.*\\d).+$", message = "비밀번호는 숫자가 포함되어야 합니다.")
	@Pattern(regexp = "^(?=.*[!@#$%^&*(){}]).+$", message = "비밀번호는 특수문자가 포함되어야 합니다.")
	@Size(min = 8, max = 15, message = "password는 8자에서 15자여야 합니다.")
	private String password;

	private String introduction;

	private boolean admin = false;

	private String adminToken = "";
}
