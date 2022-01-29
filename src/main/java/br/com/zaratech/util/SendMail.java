package br.com.zaratech.util;

import static br.com.zaratech.util.GrantControlConstants.B_STRONG_FIM;
import static br.com.zaratech.util.GrantControlConstants.B_STRONG_INICIO;
import static br.com.zaratech.util.GrantControlConstants.EMAIL_DE_ENVIO;
import static br.com.zaratech.util.GrantControlConstants.GET_CONFIG_SET;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_FROM;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_FROM_NAME;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_SMTP_HOST;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_SMTP_PASSWORD;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_SMTP_USERNAME;
import static br.com.zaratech.util.GrantControlConstants.GET_EMAIL_SUBJECT;
import static br.com.zaratech.util.GrantControlConstants.GET_URL_ENVIO;
import static br.com.zaratech.util.GrantControlConstants.LINE_SEPARATOR;
import static br.com.zaratech.util.GrantControlConstants.TEXT_HTML;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.ParametrosEmail;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoSubProduto;
import br.com.zaratech.repository.ParametrosEmailRepository;
import br.com.zaratech.repository.ParametrosRepository;
import br.com.zaratech.service.impl.UsuarioServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendMail {

    private ParametrosEmailRepository parametrosEmailRepository;
    private ParametrosRepository parametrosRepository;
    private UsuarioBean usuarioBean;

    @Autowired
    void handle(ParametrosEmailRepository parametrosEmailRepository,
            ParametrosRepository parametrosRepository,
            UsuarioBean usuarioBean) {
        this.parametrosEmailRepository = parametrosEmailRepository;
        this.parametrosRepository = parametrosRepository;
        this.usuarioBean = usuarioBean;
    }

    private Map<String, String> mapValuesMail() {
        try {
            Map<String, String> values = new HashMap<>();
            ParametrosSistema emailDeEnvio = parametrosRepository.findByChave(EMAIL_DE_ENVIO);
            ParametrosEmail parametroEmail = parametrosEmailRepository.findByEmailFrom(emailDeEnvio.getValor());

            values.put(parametroEmail.getUrlEnvio(), GET_URL_ENVIO);
            values.put(parametroEmail.getEmailFrom(), GET_EMAIL_FROM);
            values.put(parametroEmail.getEmailFromName(), GET_EMAIL_FROM_NAME);
            values.put(parametroEmail.getEmailSubject(), GET_EMAIL_SUBJECT);
            values.put(parametroEmail.getConfigSet(), GET_CONFIG_SET);
            values.put(parametroEmail.getEmailSmtpHost(), GET_EMAIL_SMTP_HOST);
            values.put(parametroEmail.getEmailSmtpUsername(), GET_EMAIL_SMTP_USERNAME);
            values.put(parametroEmail.getEmailSmtpPassword(), GET_EMAIL_SMTP_PASSWORD);
            return values;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException("mapValuesMail is null");
        }
    }

    private Session sendValidade() {
        try {
            ParametrosSistema emailDeEnvio = parametrosRepository.findByChave(EMAIL_DE_ENVIO);
            ParametrosEmail parametroEmail = parametrosEmailRepository.findByEmailFrom(emailDeEnvio.getValor());
            return Session.getDefaultInstance(getProperties(parametroEmail));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void enviarEmailPacoteAtualizacao(String destinatario, List<Cliente> lsCliente,
            List<TipoAmbiente> lsTipoAmbiente, List<TipoSubProduto> lsTipoSubProduto, String versaoHomologacao,
            String versaoPreProducao, String versaoProducao) {

        try {
            if (!Objects.isNull(sendValidade())) {
                StringBuilder sb = getStringBuilderPacoteAtualizacao(lsCliente, lsTipoAmbiente, lsTipoSubProduto,
                        versaoHomologacao, versaoPreProducao, versaoProducao);
                String bodyControleVersaoPacote = String.join(System.getProperty(LINE_SEPARATOR), sb.toString());

                MimeMessage msg = getMimeMessage(destinatario, bodyControleVersaoPacote, sendValidade());
                sendMsgMail(destinatario, msg);
            }
        } catch (Exception e) {
            log.error("Ocorreu o seguinte erro ao enviar o e-mail: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    public void enviar(String destinatario, int defineBody) throws UnsupportedEncodingException, MessagingException {

        try {
            if (!Objects.isNull(sendValidade())) {

                String bodyCadastroUsuario = String.join(
                        System.getProperty(LINE_SEPARATOR),
                        "<h1>Welcome to <strong>GrantControl</strong></h1>",
                        "<p>Your registration was successful, access and enjoy the exclusive tool for you!",
                        "<a href='" + mapValuesMail().get(GET_URL_ENVIO) + "'><strong>Click here</strong></a>");

                String bodyRecuperacaoSenha = String.join(
                        System.getProperty(LINE_SEPARATOR),
                        "<h1>Recover to access <strong>GrantControl</strong></h1>",
                        "<p>Click here in this url for recover your password!",
                        "<a href='" + mapValuesMail().get(GET_URL_ENVIO) + "recuperarAcesso?id="
                                + UsuarioServiceImpl.idRecuperacao + "'><strong>Click Here</strong></a>");

                String bodyControleVersaoPacote = String.join(
                        System.getProperty(LINE_SEPARATOR),
                        "<p>Successfully generated version control!</p>",
                        "<p>User: " + (usuarioBean.getUsuario() != null ? usuarioBean.getUsuario().getNome()
                                : "N/A" + "</p>"));

                MimeMessage msg = new MimeMessage(sendValidade());
                msg.setFrom(new InternetAddress(mapValuesMail().get(GET_EMAIL_FROM),
                        mapValuesMail().get(GET_EMAIL_FROM_NAME)));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
                msg.setSubject(mapValuesMail().get(GET_EMAIL_SUBJECT));
                if (defineBody == 0) {
                    msg.setContent(bodyCadastroUsuario, TEXT_HTML);
                } else if (defineBody == 1) {
                    msg.setContent(bodyRecuperacaoSenha, TEXT_HTML);
                } else if (defineBody == 2) {
                    msg.setContent(bodyControleVersaoPacote, TEXT_HTML);
                }
                msg.setHeader("X-SES-CONFIGURATION-SET", mapValuesMail().get(GET_CONFIG_SET));
                sendMsgMail(destinatario, msg);
            }
        } catch (Exception e) {
            log.error("Ocorreu o seguinte erro ao enviar o e-mail: " + e.getMessage());
            log.error(e.getMessage());
        }
    }

    private void sendMsgMail(String destinatario, MimeMessage msg) {
        try {
            transportSend(msg, sendValidade());
            log.info("Email enviado para " + destinatario);
        } catch (Exception e) {
            log.info("Nao foi possivel enviar o e-mail. Motivo: " + e.getMessage());
            throw new IllegalStateException("transport is null");
        }
    }

    private void transportSend(MimeMessage msg, Session sessao) throws MessagingException {
        try {
            log.info("Enviando E-mail...");
            sessao.getTransport().connect(mapValuesMail().get(GET_EMAIL_SMTP_HOST),
                    mapValuesMail().get(GET_EMAIL_SMTP_USERNAME),
                    mapValuesMail().get(GET_EMAIL_SMTP_PASSWORD));
            sessao.getTransport().sendMessage(msg, msg.getAllRecipients());
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage());
        }
    }

    private MimeMessage getMimeMessage(String destinatario, String bodyControleVersaoPacote, Session session)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mapValuesMail().get(GET_EMAIL_FROM), mapValuesMail().get(GET_EMAIL_FROM_NAME)));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        msg.setSubject(mapValuesMail().get(GET_EMAIL_SUBJECT));
        msg.setContent(bodyControleVersaoPacote, TEXT_HTML);
        msg.setHeader("X-SES-CONFIGURATION-SET", mapValuesMail().get(GET_CONFIG_SET));
        return msg;
    }

    private Properties getProperties(ParametrosEmail parametroEmail) {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", parametroEmail.getEmailPort());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        return props;
    }

    private StringBuilder getStringBuilderPacoteAtualizacao(List<Cliente> lsCliente, List<TipoAmbiente> lsTipoAmbiente,
            List<TipoSubProduto> lsTipoSubProduto, String versaoHomologacao, String versaoPreProducao,
            String versaoProducao) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p><h1>GrantControl</h1></p>");
        sb.append("<p>Usuario: <strong>" + usuarioBean.getUsuario().getNome() + B_STRONG_FIM);
        sb.append("<br>");
        sb.append("<p>Clientes atualizados: </p>");
        for (int i = 0; i < lsCliente.size(); i++) {
            sb.append(B_STRONG_INICIO + lsCliente.get(i).getNomeCliente() + B_STRONG_FIM);
        }
        sb.append("<br>");
        sb.append("<p>Tipo(s) de ambiente(s): </p>");
        for (int i = 0; i < lsTipoAmbiente.size(); i++) {
            sb.append(B_STRONG_INICIO + lsTipoAmbiente.get(i).getNomeAmbiente() + B_STRONG_FIM);
        }
        sb.append("<br>");
        sb.append("<p>Tipo(s) de sub-produto(s): </p>");
        for (int i = 0; i < lsTipoSubProduto.size(); i++) {
            sb.append(B_STRONG_INICIO + lsTipoSubProduto.get(i).getNomeSubProduto() + B_STRONG_FIM);
        }
        sb.append("<br>");
        sb.append("<p>Versao Homologacao: <strong>" + versaoHomologacao + B_STRONG_FIM);
        sb.append("<p>Versao Pre-Producao: <strong>" + versaoPreProducao + B_STRONG_FIM);
        sb.append("<p>Versao Producao: <strong>" + versaoProducao + B_STRONG_FIM);
        sb.append("<br>");
        sb.append("<p>GrantControl " + new Date() + "</p>");
        return sb;
    }
}