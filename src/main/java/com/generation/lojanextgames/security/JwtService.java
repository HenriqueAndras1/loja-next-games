package com.generation.lojanextgames.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*A Classe JwtService é responsável por criar e validar o Token JWT. O Token JWT será criado durante o processo de autenticação (login) do usuário e o 
 * mesmo será validado em todas as requisições HTTP enviadas para os endpoints protegidos, que serão definidos na Classe BasicSecurityConfig.
 * 
 */

/*
 * Esta Classe deve ser anotada com a anotação @Component, que indica que a Classe JwtService é uma Classe de Componente, 
 * que pode Injetar e Instanciar qualquer Dependência especificada na implementação da Classe, em qualquer outra Classe, 
 * sempre que necessário. Vale mencionar que os Métodos definidos nesta Classe são de extrema importância para a Spring Security.
 */

@Component 
public class JwtService {

	public static final String SECRET = "Wp8bTwoQHr5h0Sr9KELaY8bya3goBDloHKFtHteOoW+mTlQmPUI4nKJCjV414vEr";

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/*
	 * O Método createToken(Map<String, Object> claims, String userName) cria o Token JWT. 
	 * O Método recebe 2 parâmetros: uma Collection Map, chamada claims, que será utilizada para receber Claims personalizadas e um Objeto da Classe String, chamado userName,
	 *  contendo o usuário autenticado (e-mail).
	 */
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder() //O Método builder(), da Classe Jwts é responsável por criar o Token, a partir dos Métodos inseridos logo abaixo, que contém os detalhes da construção do Token JWT.
					.setClaims(claims)//O Método .setClaims(claims), da Classe Jwts é responsável por inserir as claims personalizadas no Payload do Token JWT.
					.setSubject(userName)//O Método .setSubject(userName), da Classe Jwts é responsável por inserir a claim sub (subject), preenchida com o usuario (e-mail), no Payload do Token JWT.
					.setIssuedAt(new Date(System.currentTimeMillis()))//O Método .setIssuedAt(new Date(System.currentTimeMillis())), da Classe Jwts é responsável por inserir a claim iat (issued at - data e hora da criação), preenchida com a data e a hora (incluindo os milissegundos da hora) exata do momento da criação do token, no Payload do Token JWT.
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))//O Método .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)), da Classe Jwts é responsável por inserir a claim exp (expiration - data e hora da expiração), preenchida com a data e a hora (incluindo os milissegundos da hora) exata do momento da criação do token, somada ao tempo limite do token, no Payload do Token JWT. Em nosso exemplo, o limite de expiração do Token é de 60 minutos 🡪 1 hora.
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}
