package com.zoonosys.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata a exceção customizada ResourceNotFoundException, mapeando-a para
     * o código de status HTTP 404 (Not Found).
     * * Esta exceção deve ser lançada pelos serviços quando um recurso (ex: News, User)
     * solicitado pelo ID não for encontrado no banco de dados.
     *
     * @param ex A exceção ResourceNotFoundException lançada.
     * @return Uma ResponseEntity contendo a mensagem de erro e o status HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Trata a exceção MethodArgumentNotValidException, que é lançada pelo Spring
     * quando a validação de um DTO (@Valid no Controller) falha (ex: campos @NotBlank, @Size).
     * Mapeia para o código de status HTTP 400 (Bad Request).
     *
     * Este handler é essencial para fornecer feedback detalhado ao cliente sobre
     * quais campos falharam na validação.
     *
     * @param ex A exceção MethodArgumentNotValidException contendo os erros de validação.
     * @return Uma ResponseEntity contendo um mapa (JSON) com os campos e suas mensagens de erro, e o status HTTP 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata a exceção IllegalAccessException, mapeando-a para o código de status HTTP 400 (Bad Request).
     * * Deve ser usada para erros de lógica de negócio que indicam uma requisição "ruim" por parte do cliente,
     * como uma tentativa de acesso a dados ou funcionalidade sem a permissão adequada de negócio.
     *
     * @param ex A exceção IllegalAccessException lançada.
     * @return Uma ResponseEntity contendo a mensagem de erro e o status HTTP 400.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleIllegalAccessException(BadRequestException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata todas as exceções RuntimeException não capturadas pelos handlers mais específicos,
     * atuando como uma "última camada de segurança". Mapeia para o código de status HTTP 500
     * (Internal Server Error).
     *
     * É crucial para capturar erros inesperados (como NullPointerException, falha de DB)
     * e evitar que detalhes internos vazem para o cliente. A mensagem de erro enviada ao cliente
     * deve ser genérica.
     *
     * @param ex A exceção RuntimeException (ou subclasse) lançada.
     * @return Uma ResponseEntity com uma mensagem genérica de erro interno e o status HTTP 500.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        System.err.println("Erro Interno Inesperado: " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>("Ocorreu um erro interno no servidor. Tente novamente mais tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
