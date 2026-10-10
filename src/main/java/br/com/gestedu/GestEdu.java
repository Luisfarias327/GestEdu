package br.com.gestedu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestEdu extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    private final Color BG = new Color(245, 245, 245);
    private final Color MENU = new Color(235, 235, 235);

    // IDs internos usados somente pelo programa, nunca exibidos nas tabelas.
    private int alunoSelecionado = -1;
    private int disciplinaSelecionada = -1;
    private int professorSelecionado = -1;
    private int responsavelSelecionado = -1;
    private int serieSelecionada = -1;
    private int turmaSelecionada = -1;
    private int trimestreSelecionado = -1;
    private int notaSelecionada = -1;
    private int vinculoProfessorSelecionado = -1;

    // Campos - Alunos
    private JTextField txtMatricula, txtAlunoNome, txtAlunoCpf, txtAlunoNascimento;
    private JTextField txtAlunoTelefone, txtAlunoEmail, txtAlunoTurma, txtAlunoSerie, txtAlunoResponsavel;
    private JTable tabelaAlunos;

    // Campos - Disciplinas
    private JTextField txtDiscCodigo, txtDiscNome, txtDiscDescricao;
    private JTable tabelaDisciplinas;

    // Campos - Professores
    private JTextField txtProfNome, txtProfTelefone, txtProfDisciplina;
    private JTable tabelaProfessores;

    // Campos - Responsáveis
    private JTextField txtRespNome;
    private JTable tabelaResponsaveis;

    // Campos - Séries
    private JTextField txtSerieNome;
    private JTable tabelaSeries;

    // Campos - Turmas
    private JTextField txtTurmaNome, txtTurmaSerie, txtTurmaAno;
    private JTable tabelaTurmas;

    // Campos - Notas
    private JTextField txtNotaAluno, txtNotaDisciplina, txtNotaTrimestre, txtNotaValor;
    private JTable tabelaNotas;

    // Campos - Trimestre
    private JTextField txtTrimestreNome;
    private JTable tabelaTrimestres;

    // Campos - vínculos
    private JTextField txtVinculoTurma, txtVinculoDisciplina, txtVinculoProfessor;
    private JTable tabelaVinculos;

    public GestEdu() {
        setTitle("GestEdu - Sistema de Gestão Escolar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);

        criarInterface();
        atualizarTudo();
    }

    private void criarInterface() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(BG);

        raiz.add(criarMenu(), BorderLayout.NORTH);
        raiz.add(content, BorderLayout.CENTER);

        criarTelas();

        setContentPane(raiz);
    }

    private JPanel criarMenu() {
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        menu.setBackground(MENU);
        menu.setBorder(new EmptyBorder(4, 7, 4, 7));

        String[] nomes = {
            "ALUNOS", "DISCIPLINAS", "PROFESSORES", "RESPONSÁVEIS",
            "SÉRIES", "TURMAS", "NOTAS", "TRIMESTRES", "VÍNCULOS"
        };

        for (String nome : nomes) {
            JButton b = new JButton(nome);
            b.setFont(new Font("SansSerif", Font.PLAIN, 12));
            b.setPreferredSize(new Dimension(nome.length() > 12 ? 125 : 100, 28));
            b.setFocusPainted(false);
            b.addActionListener(e -> cards.show(content, nome));
            menu.add(b);
        }

        return menu;
    }

    private void criarTelas() {
        criarTelaAlunos();
        criarTelaDisciplinas();
        criarTelaProfessores();
        criarTelaResponsaveis();
        criarTelaSeries();
        criarTelaTurmas();
        criarTelaNotas();
        criarTelaTrimestres();
        criarTelaVinculos();
    }

    // =========================================================
    // ALUNOS
    // =========================================================

    private void criarTelaAlunos() {
        JPanel tela = painelBase("ALUNOS");

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtMatricula = campo();
        txtAlunoNome = campo();
        txtAlunoCpf = campo();
        txtAlunoNascimento = campo();
        txtAlunoTelefone = campo();
        txtAlunoEmail = campo();
        txtAlunoTurma = campo();
        txtAlunoSerie = campo();
        txtAlunoResponsavel = campo();

        adicionarCampo(formulario, g, 0, 0, "MATRÍCULA:", txtMatricula);
        adicionarCampo(formulario, g, 0, 1, "NOME:", txtAlunoNome);
        adicionarCampo(formulario, g, 0, 2, "CPF:", txtAlunoCpf);
        adicionarCampo(formulario, g, 0, 3, "DATA DE NASCIMENTO:", txtAlunoNascimento);
        adicionarCampo(formulario, g, 1, 0, "TELEFONE:", txtAlunoTelefone);
        adicionarCampo(formulario, g, 1, 1, "E-MAIL:", txtAlunoEmail);
        adicionarCampo(formulario, g, 1, 2, "TURMA:", txtAlunoTurma);
        adicionarCampo(formulario, g, 1, 3, "SÉRIE:", txtAlunoSerie);
        adicionarCampo(formulario, g, 2, 0, "RESPONSÁVEL:", txtAlunoResponsavel);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar);
        botoes.add(alterar);
        botoes.add(excluir);
        botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarAluno());
        alterar.addActionListener(e -> alterarAluno());
        excluir.addActionListener(e -> excluirAluno());
        limpar.addActionListener(e -> limparAluno());

        tabelaAlunos = tabela();
        tabelaAlunos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarAluno();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);

        content.add(tela, "ALUNOS");
    }

    private void cadastrarAluno() {
        if (!validarAluno()) return;

        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);

            int serieId = obterOuCriarSerie(c, txtAlunoSerie.getText());
            int turmaId = obterOuCriarTurma(c, txtAlunoTurma.getText(), serieId, anoAtual());
            int responsavelId = obterOuCriarResponsavel(c, txtAlunoResponsavel.getText());

            String sql = """
                INSERT INTO alunos
                (matricula, nome, cpf, data_nascimento, telefone, email, id_turma, id_responsavel)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, txtMatricula.getText().trim());
                ps.setString(2, txtAlunoNome.getText().trim());
                ps.setString(3, txtAlunoCpf.getText().trim());
                ps.setDate(4, java.sql.Date.valueOf(dataSql(txtAlunoNascimento.getText().trim())));
                ps.setString(5, txtAlunoTelefone.getText().trim());
                ps.setString(6, txtAlunoEmail.getText().trim());
                ps.setInt(7, turmaId);
                if (responsavelId > 0) ps.setInt(8, responsavelId);
                else ps.setNull(8, Types.INTEGER);
                ps.executeUpdate();
            }

            c.commit();
            mensagem("Aluno cadastrado com sucesso.");
            limparAluno();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("A matrícula ou CPF informado já está cadastrado, ou algum relacionamento é inválido.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar aluno:\n" + ex.getMessage());
        }
    }

    private void alterarAluno() {
        if (alunoSelecionado < 0) {
            aviso("Selecione um aluno na tabela.");
            return;
        }
        if (!validarAluno()) return;

        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);

            int serieId = obterOuCriarSerie(c, txtAlunoSerie.getText());
            int turmaId = obterOuCriarTurma(c, txtAlunoTurma.getText(), serieId, anoAtual());
            int responsavelId = obterOuCriarResponsavel(c, txtAlunoResponsavel.getText());

            String sql = """
                UPDATE alunos SET
                matricula=?, nome=?, cpf=?, data_nascimento=?, telefone=?, email=?,
                id_turma=?, id_responsavel=?
                WHERE id=?
            """;

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, txtMatricula.getText().trim());
                ps.setString(2, txtAlunoNome.getText().trim());
                ps.setString(3, txtAlunoCpf.getText().trim());
                ps.setDate(4, java.sql.Date.valueOf(dataSql(txtAlunoNascimento.getText().trim())));
                ps.setString(5, txtAlunoTelefone.getText().trim());
                ps.setString(6, txtAlunoEmail.getText().trim());
                ps.setInt(7, turmaId);
                if (responsavelId > 0) ps.setInt(8, responsavelId);
                else ps.setNull(8, Types.INTEGER);
                ps.setInt(9, alunoSelecionado);
                ps.executeUpdate();
            }

            c.commit();
            mensagem("Aluno alterado com sucesso.");
            limparAluno();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar aluno:\n" + ex.getMessage());
        }
    }

    private void excluirAluno() {
        if (alunoSelecionado < 0) {
            aviso("Selecione um aluno na tabela.");
            return;
        }

        if (!confirmar("Deseja realmente excluir este aluno?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement("DELETE FROM alunos WHERE id=?")) {

            ps.setInt(1, alunoSelecionado);
            ps.executeUpdate();

            mensagem("Aluno excluído.");
            limparAluno();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não foi possível excluir o aluno porque existem registros relacionados.");
        } catch (Exception ex) {
            erro("Erro ao excluir aluno:\n" + ex.getMessage());
        }
    }

    private void selecionarAluno() {
        int linha = tabelaAlunos.getSelectedRow();
        if (linha < 0) return;

        alunoSelecionado = (int) tabelaAlunos.getValueAt(linha, 0);

        txtMatricula.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 1)));
        txtAlunoNome.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 2)));
        txtAlunoCpf.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 3)));
        txtAlunoNascimento.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 4)));
        txtAlunoTelefone.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 5)));
        txtAlunoEmail.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 6)));
        txtAlunoTurma.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 7)));
        txtAlunoSerie.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 8)));
        txtAlunoResponsavel.setText(String.valueOf(tabelaAlunos.getValueAt(linha, 9)));
    }

    private void atualizarAlunos() {
        String sql = """
            SELECT a.id, a.matricula, a.nome, a.cpf,
                   DATE_FORMAT(a.data_nascimento, '%d/%m/%Y') AS nascimento,
                   a.telefone, COALESCE(a.email, '') AS email,
                   t.nome AS turma, s.nome AS serie,
                   COALESCE(r.nome, '') AS responsavel
            FROM alunos a
            JOIN turmas t ON a.id_turma=t.id
            JOIN serie s ON t.id_serie=s.id
            LEFT JOIN responsavel r ON a.id_responsavel=r.id
            ORDER BY a.nome
        """;

        preencherTabela(tabelaAlunos, sql,
                "ID_INTERNO", "MATRÍCULA", "NOME", "CPF", "DATA DE NASCIMENTO",
                "TELEFONE", "E-MAIL", "TURMA", "SÉRIE", "RESPONSÁVEL");
        ocultarColuna(tabelaAlunos, 0);
    }

    private boolean validarAluno() {
        if (vazio(txtMatricula) || vazio(txtAlunoNome) || vazio(txtAlunoCpf)
                || vazio(txtAlunoNascimento) || vazio(txtAlunoTelefone)
                || vazio(txtAlunoTurma) || vazio(txtAlunoSerie)) {
            aviso("Preencha matrícula, nome, CPF, nascimento, telefone, turma e série.");
            return false;
        }

        if (!txtAlunoResponsavel.getText().trim().isEmpty()
                && txtAlunoResponsavel.getText().trim().length() < 2) {
            aviso("Informe um nome válido para o responsável.");
            return false;
        }

        try {
            dataSql(txtAlunoNascimento.getText().trim());
        } catch (Exception e) {
            aviso("Data inválida. Use o formato DD/MM/AAAA.");
            return false;
        }

        return true;
    }

    private void limparAluno() {
        alunoSelecionado = -1;
        limpar(txtMatricula, txtAlunoNome, txtAlunoCpf, txtAlunoNascimento,
                txtAlunoTelefone, txtAlunoEmail, txtAlunoTurma, txtAlunoSerie,
                txtAlunoResponsavel);
        tabelaAlunos.clearSelection();
    }

    // =========================================================
    // DISCIPLINAS
    // =========================================================

    private void criarTelaDisciplinas() {
        JPanel tela = painelBase("DISCIPLINAS");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtDiscCodigo = campo();
        txtDiscNome = campo();
        txtDiscDescricao = campo();

        adicionarCampo(formulario, g, 0, 0, "CÓDIGO:", txtDiscCodigo);
        adicionarCampo(formulario, g, 0, 1, "NOME:", txtDiscNome);
        adicionarCampo(formulario, g, 0, 2, "DESCRIÇÃO:", txtDiscDescricao);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarDisciplina());
        alterar.addActionListener(e -> alterarDisciplina());
        excluir.addActionListener(e -> excluirDisciplina());
        limpar.addActionListener(e -> limparDisciplina());

        tabelaDisciplinas = tabela();
        tabelaDisciplinas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarDisciplina();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaDisciplinas), BorderLayout.CENTER);
        content.add(tela, "DISCIPLINAS");
    }

    private void cadastrarDisciplina() {
        if (vazio(txtDiscCodigo) || vazio(txtDiscNome)) {
            aviso("Informe o código e o nome da disciplina.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO disciplinas (codigo,nome,descricao) VALUES (?,?,?)")) {

            ps.setString(1, txtDiscCodigo.getText().trim().toUpperCase());
            ps.setString(2, txtDiscNome.getText().trim());
            ps.setString(3, txtDiscDescricao.getText().trim());
            ps.executeUpdate();

            mensagem("Disciplina cadastrada.");
            limparDisciplina();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Já existe uma disciplina com esse código.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar disciplina:\n" + ex.getMessage());
        }
    }

    private void alterarDisciplina() {
        if (disciplinaSelecionada < 0) {
            aviso("Selecione uma disciplina na tabela.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE disciplinas SET codigo=?, nome=?, descricao=? WHERE id=?")) {

            ps.setString(1, txtDiscCodigo.getText().trim().toUpperCase());
            ps.setString(2, txtDiscNome.getText().trim());
            ps.setString(3, txtDiscDescricao.getText().trim());
            ps.setInt(4, disciplinaSelecionada);
            ps.executeUpdate();

            mensagem("Disciplina alterada.");
            limparDisciplina();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar disciplina:\n" + ex.getMessage());
        }
    }

    private void excluirDisciplina() {
        if (disciplinaSelecionada < 0) {
            aviso("Selecione uma disciplina na tabela.");
            return;
        }
        if (!confirmar("Deseja excluir esta disciplina?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM disciplinas WHERE id=?")) {

            ps.setInt(1, disciplinaSelecionada);
            ps.executeUpdate();

            mensagem("Disciplina excluída.");
            limparDisciplina();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não é possível excluir esta disciplina porque existem vínculos ou notas relacionados.");
        } catch (Exception ex) {
            erro("Erro ao excluir disciplina:\n" + ex.getMessage());
        }
    }

    private void selecionarDisciplina() {
        int linha = tabelaDisciplinas.getSelectedRow();
        if (linha < 0) return;

        disciplinaSelecionada = (int) tabelaDisciplinas.getValueAt(linha, 0);
        txtDiscCodigo.setText(String.valueOf(tabelaDisciplinas.getValueAt(linha, 1)));
        txtDiscNome.setText(String.valueOf(tabelaDisciplinas.getValueAt(linha, 2)));
        txtDiscDescricao.setText(String.valueOf(tabelaDisciplinas.getValueAt(linha, 3)));
    }

    private void atualizarDisciplinas() {
        String sql = """
            SELECT id, codigo, nome, descricao
            FROM disciplinas
            ORDER BY codigo
        """;
        preencherTabela(tabelaDisciplinas, sql,
                "ID_INTERNO", "CÓDIGO", "NOME", "DESCRIÇÃO");
        ocultarColuna(tabelaDisciplinas, 0);
    }

    private void limparDisciplina() {
        disciplinaSelecionada = -1;
        limpar(txtDiscCodigo, txtDiscNome, txtDiscDescricao);
        tabelaDisciplinas.clearSelection();
    }

    // =========================================================
    // PROFESSORES
    // =========================================================

    private void criarTelaProfessores() {
        JPanel tela = painelBase("PROFESSORES");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtProfNome = campo();
        txtProfTelefone = campo();
        txtProfDisciplina = campo();

        adicionarCampo(formulario, g, 0, 0, "NOME:", txtProfNome);
        adicionarCampo(formulario, g, 0, 1, "TELEFONE:", txtProfTelefone);
        adicionarCampo(formulario, g, 0, 2, "CÓDIGO DA DISCIPLINA:", txtProfDisciplina);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarProfessor());
        alterar.addActionListener(e -> alterarProfessor());
        excluir.addActionListener(e -> excluirProfessor());
        limpar.addActionListener(e -> limparProfessor());

        tabelaProfessores = tabela();
        tabelaProfessores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarProfessor();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaProfessores), BorderLayout.CENTER);
        content.add(tela, "PROFESSORES");
    }

    private void cadastrarProfessor() {
        if (vazio(txtProfNome) || vazio(txtProfTelefone) || vazio(txtProfDisciplina)) {
            aviso("Preencha nome, telefone e código da disciplina.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            int disciplinaId = buscarDisciplinaId(c, txtProfDisciplina.getText().trim());

            if (disciplinaId < 0) {
                aviso("O código da disciplina não foi encontrado.");
                return;
            }

            c.setAutoCommit(false);

            int professorId;

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO professores (nome,telefone) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, txtProfNome.getText().trim());
                ps.setString(2, txtProfTelefone.getText().trim());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    professorId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO professor_disciplina (id_professor,id_disciplina) VALUES (?,?)")) {
                ps.setInt(1, professorId);
                ps.setInt(2, disciplinaId);
                ps.executeUpdate();
            }

            c.commit();

            mensagem("Professor cadastrado com a disciplina.");
            limparProfessor();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Esse professor já possui esse vínculo com a disciplina.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar professor:\n" + ex.getMessage());
        }
    }

    private void alterarProfessor() {
        if (professorSelecionado < 0) {
            aviso("Selecione um professor na tabela.");
            return;
        }

        if (vazio(txtProfNome) || vazio(txtProfTelefone) || vazio(txtProfDisciplina)) {
            aviso("Preencha todos os campos.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            int disciplinaId = buscarDisciplinaId(c, txtProfDisciplina.getText().trim());

            if (disciplinaId < 0) {
                aviso("Código da disciplina não encontrado.");
                return;
            }

            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE professores SET nome=?, telefone=? WHERE id=?")) {
                ps.setString(1, txtProfNome.getText().trim());
                ps.setString(2, txtProfTelefone.getText().trim());
                ps.setInt(3, professorSelecionado);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM professor_disciplina WHERE id_professor=?")) {
                ps.setInt(1, professorSelecionado);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO professor_disciplina (id_professor,id_disciplina) VALUES (?,?)")) {
                ps.setInt(1, professorSelecionado);
                ps.setInt(2, disciplinaId);
                ps.executeUpdate();
            }

            c.commit();

            mensagem("Professor alterado.");
            limparProfessor();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar professor:\n" + ex.getMessage());
        }
    }

    private void excluirProfessor() {
        if (professorSelecionado < 0) {
            aviso("Selecione um professor na tabela.");
            return;
        }

        if (!confirmar("Deseja excluir este professor?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM professores WHERE id=?")) {

            ps.setInt(1, professorSelecionado);
            ps.executeUpdate();

            mensagem("Professor excluído.");
            limparProfessor();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não foi possível excluir o professor porque ele está ligado a uma turma/disciplina.");
        } catch (Exception ex) {
            erro("Erro ao excluir professor:\n" + ex.getMessage());
        }
    }

    private void selecionarProfessor() {
        int linha = tabelaProfessores.getSelectedRow();
        if (linha < 0) return;

        professorSelecionado = (int) tabelaProfessores.getValueAt(linha, 0);
        txtProfNome.setText(String.valueOf(tabelaProfessores.getValueAt(linha, 1)));
        txtProfTelefone.setText(String.valueOf(tabelaProfessores.getValueAt(linha, 2)));
        txtProfDisciplina.setText(String.valueOf(tabelaProfessores.getValueAt(linha, 3)));
    }

    private void atualizarProfessores() {
        String sql = """
            SELECT p.id, p.nome, p.telefone,
                   COALESCE(GROUP_CONCAT(d.codigo ORDER BY d.codigo SEPARATOR ', '), '') AS disciplina
            FROM professores p
            LEFT JOIN professor_disciplina pd ON p.id=pd.id_professor
            LEFT JOIN disciplinas d ON pd.id_disciplina=d.id
            GROUP BY p.id, p.nome, p.telefone
            ORDER BY p.nome
        """;
        preencherTabela(tabelaProfessores, sql,
                "ID_INTERNO", "NOME", "TELEFONE", "DISCIPLINA");
        ocultarColuna(tabelaProfessores, 0);
    }

    private void limparProfessor() {
        professorSelecionado = -1;
        limpar(txtProfNome, txtProfTelefone, txtProfDisciplina);
        tabelaProfessores.clearSelection();
    }

    // =========================================================
    // RESPONSÁVEIS
    // =========================================================

    private void criarTelaResponsaveis() {
        JPanel tela = painelBase("RESPONSÁVEIS");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtRespNome = campo();
        adicionarCampo(formulario, g, 0, 0, "NOME:", txtRespNome);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarResponsavel());
        alterar.addActionListener(e -> alterarResponsavel());
        excluir.addActionListener(e -> excluirResponsavel());
        limpar.addActionListener(e -> limparResponsavel());

        tabelaResponsaveis = tabela();
        tabelaResponsaveis.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarResponsavel();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaResponsaveis), BorderLayout.CENTER);
        content.add(tela, "RESPONSÁVEIS");
    }

    private void cadastrarResponsavel() {
        if (vazio(txtRespNome)) {
            aviso("Informe o nome do responsável.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO responsavel (nome) VALUES (?)")) {

            ps.setString(1, txtRespNome.getText().trim());
            ps.executeUpdate();

            mensagem("Responsável cadastrado.");
            limparResponsavel();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Já existe um responsável com esse nome.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar responsável:\n" + ex.getMessage());
        }
    }

    private void alterarResponsavel() {
        if (responsavelSelecionado < 0) {
            aviso("Selecione um responsável.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE responsavel SET nome=? WHERE id=?")) {

            ps.setString(1, txtRespNome.getText().trim());
            ps.setInt(2, responsavelSelecionado);
            ps.executeUpdate();

            mensagem("Responsável alterado.");
            limparResponsavel();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar responsável:\n" + ex.getMessage());
        }
    }

    private void excluirResponsavel() {
        if (responsavelSelecionado < 0) {
            aviso("Selecione um responsável.");
            return;
        }

        if (!confirmar("Deseja excluir este responsável?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM responsavel WHERE id=?")) {

            ps.setInt(1, responsavelSelecionado);
            ps.executeUpdate();

            mensagem("Responsável excluído.");
            limparResponsavel();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao excluir responsável:\n" + ex.getMessage());
        }
    }

    private void selecionarResponsavel() {
        int linha = tabelaResponsaveis.getSelectedRow();
        if (linha < 0) return;

        responsavelSelecionado = (int) tabelaResponsaveis.getValueAt(linha, 0);
        txtRespNome.setText(String.valueOf(tabelaResponsaveis.getValueAt(linha, 1)));
    }

    private void atualizarResponsaveis() {
        preencherTabela(tabelaResponsaveis,
                "SELECT id, nome FROM responsavel ORDER BY nome",
                "ID_INTERNO", "NOME");
        ocultarColuna(tabelaResponsaveis, 0);
    }

    private void limparResponsavel() {
        responsavelSelecionado = -1;
        limpar(txtRespNome);
        tabelaResponsaveis.clearSelection();
    }

    // =========================================================
    // SÉRIES
    // =========================================================

    private void criarTelaSeries() {
        JPanel tela = painelBase("SÉRIES");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtSerieNome = campo();
        adicionarCampo(formulario, g, 0, 0, "NOME DA SÉRIE:", txtSerieNome);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarSerie());
        alterar.addActionListener(e -> alterarSerie());
        excluir.addActionListener(e -> excluirSerie());
        limpar.addActionListener(e -> limparSerie());

        tabelaSeries = tabela();
        tabelaSeries.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarSerie();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaSeries), BorderLayout.CENTER);
        content.add(tela, "SÉRIES");
    }

    private void cadastrarSerie() {
        if (vazio(txtSerieNome)) {
            aviso("Informe o nome da série.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO serie (nome) VALUES (?)")) {

            ps.setString(1, txtSerieNome.getText().trim());
            ps.executeUpdate();

            mensagem("Série cadastrada.");
            limparSerie();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Essa série já existe.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar série:\n" + ex.getMessage());
        }
    }

    private void alterarSerie() {
        if (serieSelecionada < 0) {
            aviso("Selecione uma série.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE serie SET nome=? WHERE id=?")) {

            ps.setString(1, txtSerieNome.getText().trim());
            ps.setInt(2, serieSelecionada);
            ps.executeUpdate();

            mensagem("Série alterada.");
            limparSerie();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar série:\n" + ex.getMessage());
        }
    }

    private void excluirSerie() {
        if (serieSelecionada < 0) {
            aviso("Selecione uma série.");
            return;
        }

        if (!confirmar("Deseja excluir esta série?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM serie WHERE id=?")) {

            ps.setInt(1, serieSelecionada);
            ps.executeUpdate();

            mensagem("Série excluída.");
            limparSerie();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não é possível excluir uma série que possui turmas.");
        } catch (Exception ex) {
            erro("Erro ao excluir série:\n" + ex.getMessage());
        }
    }

    private void selecionarSerie() {
        int linha = tabelaSeries.getSelectedRow();
        if (linha < 0) return;

        serieSelecionada = (int) tabelaSeries.getValueAt(linha, 0);
        txtSerieNome.setText(String.valueOf(tabelaSeries.getValueAt(linha, 1)));
    }

    private void atualizarSeries() {
        preencherTabela(tabelaSeries,
                "SELECT id, nome FROM serie ORDER BY nome",
                "ID_INTERNO", "SÉRIE");
        ocultarColuna(tabelaSeries, 0);
    }

    private void limparSerie() {
        serieSelecionada = -1;
        limpar(txtSerieNome);
        tabelaSeries.clearSelection();
    }

    // =========================================================
    // TURMAS
    // =========================================================

    private void criarTelaTurmas() {
        JPanel tela = painelBase("TURMAS");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtTurmaNome = campo();
        txtTurmaSerie = campo();
        txtTurmaAno = campo();

        adicionarCampo(formulario, g, 0, 0, "TURMA:", txtTurmaNome);
        adicionarCampo(formulario, g, 0, 1, "SÉRIE:", txtTurmaSerie);
        adicionarCampo(formulario, g, 0, 2, "ANO LETIVO:", txtTurmaAno);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarTurma());
        alterar.addActionListener(e -> alterarTurma());
        excluir.addActionListener(e -> excluirTurma());
        limpar.addActionListener(e -> limparTurma());

        tabelaTurmas = tabela();
        tabelaTurmas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarTurma();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaTurmas), BorderLayout.CENTER);
        content.add(tela, "TURMAS");
    }

    private void cadastrarTurma() {
        if (vazio(txtTurmaNome) || vazio(txtTurmaSerie) || vazio(txtTurmaAno)) {
            aviso("Preencha turma, série e ano letivo.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            int serieId = obterOuCriarSerie(c, txtTurmaSerie.getText());
            int ano = Integer.parseInt(txtTurmaAno.getText().trim());

            if (obterTurmaId(c, txtTurmaNome.getText().trim(), serieId, ano) > 0) {
                aviso("Essa turma já existe.");
                return;
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO turmas (nome,id_serie,ano_letivo) VALUES (?,?,?)")) {
                ps.setString(1, txtTurmaNome.getText().trim().toUpperCase());
                ps.setInt(2, serieId);
                ps.setInt(3, ano);
                ps.executeUpdate();
            }

            mensagem("Turma cadastrada.");
            limparTurma();
            atualizarTudo();

        } catch (NumberFormatException ex) {
            aviso("O ano letivo deve ser um número.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar turma:\n" + ex.getMessage());
        }
    }

    private void alterarTurma() {
        if (turmaSelecionada < 0) {
            aviso("Selecione uma turma.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            int serieId = obterOuCriarSerie(c, txtTurmaSerie.getText());
            int ano = Integer.parseInt(txtTurmaAno.getText().trim());

            String sql = """
                UPDATE turmas SET nome=?, id_serie=?, ano_letivo=?
                WHERE id=?
            """;

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, txtTurmaNome.getText().trim().toUpperCase());
                ps.setInt(2, serieId);
                ps.setInt(3, ano);
                ps.setInt(4, turmaSelecionada);
                ps.executeUpdate();
            }

            mensagem("Turma alterada.");
            limparTurma();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar turma:\n" + ex.getMessage());
        }
    }

    private void excluirTurma() {
        if (turmaSelecionada < 0) {
            aviso("Selecione uma turma.");
            return;
        }

        if (!confirmar("Deseja excluir esta turma?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM turmas WHERE id=?")) {

            ps.setInt(1, turmaSelecionada);
            ps.executeUpdate();

            mensagem("Turma excluída.");
            limparTurma();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não é possível excluir uma turma que possui alunos ou vínculos.");
        } catch (Exception ex) {
            erro("Erro ao excluir turma:\n" + ex.getMessage());
        }
    }

    private void selecionarTurma() {
        int linha = tabelaTurmas.getSelectedRow();
        if (linha < 0) return;

        turmaSelecionada = (int) tabelaTurmas.getValueAt(linha, 0);
        txtTurmaNome.setText(String.valueOf(tabelaTurmas.getValueAt(linha, 1)));
        txtTurmaSerie.setText(String.valueOf(tabelaTurmas.getValueAt(linha, 2)));
        txtTurmaAno.setText(String.valueOf(tabelaTurmas.getValueAt(linha, 3)));
    }

    private void atualizarTurmas() {
        String sql = """
            SELECT t.id, t.nome, s.nome AS serie, t.ano_letivo
            FROM turmas t
            JOIN serie s ON t.id_serie=s.id
            ORDER BY t.ano_letivo DESC, s.nome, t.nome
        """;

        preencherTabela(tabelaTurmas, sql,
                "ID_INTERNO", "TURMA", "SÉRIE", "ANO LETIVO");
        ocultarColuna(tabelaTurmas, 0);
    }

    private void limparTurma() {
        turmaSelecionada = -1;
        limpar(txtTurmaNome, txtTurmaSerie, txtTurmaAno);
        tabelaTurmas.clearSelection();
    }

    // =========================================================
    // TRIMESTRES
    // =========================================================

    private void criarTelaTrimestres() {
        JPanel tela = painelBase("TRIMESTRES");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtTrimestreNome = campo();
        adicionarCampo(formulario, g, 0, 0, "NOME:", txtTrimestreNome);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarTrimestre());
        alterar.addActionListener(e -> alterarTrimestre());
        excluir.addActionListener(e -> excluirTrimestre());
        limpar.addActionListener(e -> limparTrimestre());

        tabelaTrimestres = tabela();
        tabelaTrimestres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarTrimestre();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaTrimestres), BorderLayout.CENTER);
        content.add(tela, "TRIMESTRES");
    }

    private void cadastrarTrimestre() {
        if (vazio(txtTrimestreNome)) {
            aviso("Informe o nome do trimestre.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO trimestre (nome) VALUES (?)")) {

            ps.setString(1, txtTrimestreNome.getText().trim());
            ps.executeUpdate();

            mensagem("Trimestre cadastrado.");
            limparTrimestre();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Esse trimestre já existe.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar trimestre:\n" + ex.getMessage());
        }
    }

    private void alterarTrimestre() {
        if (trimestreSelecionado < 0) {
            aviso("Selecione um trimestre.");
            return;
        }

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE trimestre SET nome=? WHERE id=?")) {

            ps.setString(1, txtTrimestreNome.getText().trim());
            ps.setInt(2, trimestreSelecionado);
            ps.executeUpdate();

            mensagem("Trimestre alterado.");
            limparTrimestre();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao alterar trimestre:\n" + ex.getMessage());
        }
    }

    private void excluirTrimestre() {
        if (trimestreSelecionado < 0) {
            aviso("Selecione um trimestre.");
            return;
        }

        if (!confirmar("Deseja excluir este trimestre?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM trimestre WHERE id=?")) {

            ps.setInt(1, trimestreSelecionado);
            ps.executeUpdate();

            mensagem("Trimestre excluído.");
            limparTrimestre();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Não é possível excluir um trimestre que possui notas.");
        } catch (Exception ex) {
            erro("Erro ao excluir trimestre:\n" + ex.getMessage());
        }
    }

    private void selecionarTrimestre() {
        int linha = tabelaTrimestres.getSelectedRow();
        if (linha < 0) return;

        trimestreSelecionado = (int) tabelaTrimestres.getValueAt(linha, 0);
        txtTrimestreNome.setText(String.valueOf(tabelaTrimestres.getValueAt(linha, 1)));
    }

    private void atualizarTrimestres() {
        preencherTabela(tabelaTrimestres,
                "SELECT id, nome FROM trimestre ORDER BY id",
                "ID_INTERNO", "TRIMESTRE");
        ocultarColuna(tabelaTrimestres, 0);
    }

    private void limparTrimestre() {
        trimestreSelecionado = -1;
        limpar(txtTrimestreNome);
        tabelaTrimestres.clearSelection();
    }

    // =========================================================
    // NOTAS
    // =========================================================

    private void criarTelaNotas() {
        JPanel tela = painelBase("NOTAS");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtNotaAluno = campo();
        txtNotaDisciplina = campo();
        txtNotaTrimestre = campo();
        txtNotaValor = campo();

        adicionarCampo(formulario, g, 0, 0, "MATRÍCULA DO ALUNO:", txtNotaAluno);
        adicionarCampo(formulario, g, 0, 1, "CÓDIGO DA DISCIPLINA:", txtNotaDisciplina);
        adicionarCampo(formulario, g, 0, 2, "TRIMESTRE:", txtNotaTrimestre);
        adicionarCampo(formulario, g, 0, 3, "NOTA:", txtNotaValor);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton alterar = botao("ALTERAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarNota());
        alterar.addActionListener(e -> alterarNota());
        excluir.addActionListener(e -> excluirNota());
        limpar.addActionListener(e -> limparNota());

        tabelaNotas = tabela();
        tabelaNotas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarNota();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaNotas), BorderLayout.CENTER);
        content.add(tela, "NOTAS");
    }

    private void cadastrarNota() {
        if (vazio(txtNotaAluno) || vazio(txtNotaDisciplina)
                || vazio(txtNotaTrimestre) || vazio(txtNotaValor)) {
            aviso("Preencha aluno, disciplina, trimestre e nota.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            int alunoId = buscarAlunoId(c, txtNotaAluno.getText().trim());
            int disciplinaId = buscarDisciplinaId(c, txtNotaDisciplina.getText().trim());
            int trimestreId = buscarTrimestreId(c, txtNotaTrimestre.getText().trim());
            double nota = Double.parseDouble(txtNotaValor.getText().trim().replace(",", "."));

            if (alunoId < 0) {
                aviso("Matrícula do aluno não encontrada.");
                return;
            }
            if (disciplinaId < 0) {
                aviso("Código da disciplina não encontrado.");
                return;
            }
            if (trimestreId < 0) {
                aviso("Trimestre não encontrado.");
                return;
            }
            if (nota < 0 || nota > 10) {
                aviso("A nota deve estar entre 0 e 10.");
                return;
            }

            String sql = """
                INSERT INTO nota_trimestral
                (id_aluno,id_disciplina,id_trimestre,nota)
                VALUES (?,?,?,?)
            """;

            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, alunoId);
                ps.setInt(2, disciplinaId);
                ps.setInt(3, trimestreId);
                ps.setDouble(4, nota);
                ps.executeUpdate();
            }

            mensagem("Nota cadastrada.");
            limparNota();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Já existe uma nota desse aluno para essa disciplina e trimestre.");
        } catch (NumberFormatException ex) {
            aviso("Informe uma nota válida, por exemplo: 8,5");
        } catch (Exception ex) {
            erro("Erro ao cadastrar nota:\n" + ex.getMessage());
        }
    }

    private void alterarNota() {
        if (notaSelecionada < 0) {
            aviso("Selecione uma nota.");
            return;
        }

        try {
            double nota = Double.parseDouble(txtNotaValor.getText().trim().replace(",", "."));
            if (nota < 0 || nota > 10) {
                aviso("A nota deve estar entre 0 e 10.");
                return;
            }

            try (Connection c = Conexao.conectar();
                 PreparedStatement ps = c.prepareStatement(
                         "UPDATE nota_trimestral SET nota=? WHERE id=?")) {

                ps.setDouble(1, nota);
                ps.setInt(2, notaSelecionada);
                ps.executeUpdate();

                mensagem("Nota alterada.");
                limparNota();
                atualizarTudo();
            }

        } catch (Exception ex) {
            erro("Erro ao alterar nota:\n" + ex.getMessage());
        }
    }

    private void excluirNota() {
        if (notaSelecionada < 0) {
            aviso("Selecione uma nota.");
            return;
        }

        if (!confirmar("Deseja excluir esta nota?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM nota_trimestral WHERE id=?")) {

            ps.setInt(1, notaSelecionada);
            ps.executeUpdate();

            mensagem("Nota excluída.");
            limparNota();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao excluir nota:\n" + ex.getMessage());
        }
    }

    private void selecionarNota() {
        int linha = tabelaNotas.getSelectedRow();
        if (linha < 0) return;

        notaSelecionada = (int) tabelaNotas.getValueAt(linha, 0);
        txtNotaAluno.setText(String.valueOf(tabelaNotas.getValueAt(linha, 1)));
        txtNotaDisciplina.setText(String.valueOf(tabelaNotas.getValueAt(linha, 2)));
        txtNotaTrimestre.setText(String.valueOf(tabelaNotas.getValueAt(linha, 3)));
        txtNotaValor.setText(String.valueOf(tabelaNotas.getValueAt(linha, 4)));
    }

    private void atualizarNotas() {
        String sql = """
            SELECT n.id,
                   a.matricula,
                   d.codigo,
                   t.nome,
                   REPLACE(FORMAT(n.nota,2), '.', ',') AS nota
            FROM nota_trimestral n
            JOIN alunos a ON n.id_aluno=a.id
            JOIN disciplinas d ON n.id_disciplina=d.id
            JOIN trimestre t ON n.id_trimestre=t.id
            ORDER BY a.nome, d.codigo, t.id
        """;

        preencherTabela(tabelaNotas, sql,
                "ID_INTERNO", "MATRÍCULA", "DISCIPLINA", "TRIMESTRE", "NOTA");
        ocultarColuna(tabelaNotas, 0);
    }

    private void limparNota() {
        notaSelecionada = -1;
        limpar(txtNotaAluno, txtNotaDisciplina, txtNotaTrimestre, txtNotaValor);
        tabelaNotas.clearSelection();
    }

    // =========================================================
    // VÍNCULOS
    // =========================================================

    private void criarTelaVinculos() {
        JPanel tela = painelBase("VÍNCULOS");
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BG);
        GridBagConstraints g = gbc();

        txtVinculoTurma = campo();
        txtVinculoDisciplina = campo();
        txtVinculoProfessor = campo();

        adicionarCampo(formulario, g, 0, 0, "TURMA:", txtVinculoTurma);
        adicionarCampo(formulario, g, 0, 1, "CÓDIGO DA DISCIPLINA:", txtVinculoDisciplina);
        adicionarCampo(formulario, g, 0, 2, "PROFESSOR:", txtVinculoProfessor);

        JPanel botoes = botoes();
        JButton cadastrar = botao("CADASTRAR");
        JButton excluir = botao("EXCLUIR");
        JButton limpar = botao("LIMPAR");
        botoes.add(cadastrar); botoes.add(excluir); botoes.add(limpar);

        cadastrar.addActionListener(e -> cadastrarVinculo());
        excluir.addActionListener(e -> excluirVinculo());
        limpar.addActionListener(e -> limparVinculo());

        tabelaVinculos = tabela();
        tabelaVinculos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarVinculo();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(BG);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botoes, BorderLayout.SOUTH);

        tela.add(superior, BorderLayout.NORTH);
        tela.add(new JScrollPane(tabelaVinculos), BorderLayout.CENTER);
        content.add(tela, "VÍNCULOS");
    }

    private void cadastrarVinculo() {
        if (vazio(txtVinculoTurma) || vazio(txtVinculoDisciplina) || vazio(txtVinculoProfessor)) {
            aviso("Informe turma, código da disciplina e professor.");
            return;
        }

        try (Connection c = Conexao.conectar()) {
            String[] partes = txtVinculoTurma.getText().trim().split("\\s+", 2);

            int turmaId;
            if (partes.length == 2) {
                int serieId = buscarSerieId(c, partes[1]);
                turmaId = serieId > 0
                        ? obterTurmaId(c, partes[0], serieId, anoAtual())
                        : -1;
            } else {
                turmaId = buscarTurmaPorNome(c, partes[0]);
            }

            int disciplinaId = buscarDisciplinaId(c, txtVinculoDisciplina.getText().trim());
            int professorId = buscarProfessorIdPorNome(c, txtVinculoProfessor.getText().trim());

            if (turmaId < 0) {
                aviso("Turma não encontrada. Use, por exemplo, A 9º.");
                return;
            }
            if (disciplinaId < 0) {
                aviso("Disciplina não encontrada.");
                return;
            }
            if (professorId < 0) {
                aviso("Professor não encontrado.");
                return;
            }

            c.setAutoCommit(false);

            // Garante o vínculo professor-disciplina.
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT IGNORE INTO professor_disciplina
                    (id_professor,id_disciplina) VALUES (?,?)
            """)) {
                ps.setInt(1, professorId);
                ps.setInt(2, disciplinaId);
                ps.executeUpdate();
            }

            // Garante o vínculo turma-disciplina.
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT IGNORE INTO turma_disciplina
                    (id_turma,id_disciplina) VALUES (?,?)
            """)) {
                ps.setInt(1, turmaId);
                ps.setInt(2, disciplinaId);
                ps.executeUpdate();
            }

            // Cria o vínculo completo.
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO professor_turma_disciplina
                    (id_turma,id_disciplina,id_professor)
                    VALUES (?,?,?)
            """)) {
                ps.setInt(1, turmaId);
                ps.setInt(2, disciplinaId);
                ps.setInt(3, professorId);
                ps.executeUpdate();
            }

            c.commit();

            mensagem("Vínculo cadastrado.");
            limparVinculo();
            atualizarTudo();

        } catch (SQLIntegrityConstraintViolationException ex) {
            erro("Esse vínculo já existe.");
        } catch (Exception ex) {
            erro("Erro ao cadastrar vínculo:\n" + ex.getMessage());
        }
    }

    private void excluirVinculo() {
        if (vinculoProfessorSelecionado < 0) {
            aviso("Selecione um vínculo.");
            return;
        }

        if (!confirmar("Deseja excluir este vínculo?")) return;

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM professor_turma_disciplina WHERE id=?")) {

            ps.setInt(1, vinculoProfessorSelecionado);
            ps.executeUpdate();

            mensagem("Vínculo excluído.");
            limparVinculo();
            atualizarTudo();

        } catch (Exception ex) {
            erro("Erro ao excluir vínculo:\n" + ex.getMessage());
        }
    }

    private void selecionarVinculo() {
        int linha = tabelaVinculos.getSelectedRow();
        if (linha < 0) return;

        vinculoProfessorSelecionado = (int) tabelaVinculos.getValueAt(linha, 0);
        txtVinculoTurma.setText(String.valueOf(tabelaVinculos.getValueAt(linha, 1))
                + " " + String.valueOf(tabelaVinculos.getValueAt(linha, 2)));
        txtVinculoDisciplina.setText(String.valueOf(tabelaVinculos.getValueAt(linha, 3)));
        txtVinculoProfessor.setText(String.valueOf(tabelaVinculos.getValueAt(linha, 4)));
    }

    private void atualizarVinculos() {
        String sql = """
            SELECT ptd.id,
                   tu.nome AS turma,
                   s.nome AS serie,
                   d.codigo AS disciplina,
                   p.nome AS professor
            FROM professor_turma_disciplina ptd
            JOIN turmas tu ON ptd.id_turma=tu.id
            JOIN serie s ON tu.id_serie=s.id
            JOIN disciplinas d ON ptd.id_disciplina=d.id
            JOIN professores p ON ptd.id_professor=p.id
            ORDER BY s.nome, tu.nome, d.codigo, p.nome
        """;

        preencherTabela(tabelaVinculos, sql,
                "ID_INTERNO", "TURMA", "SÉRIE", "DISCIPLINA", "PROFESSOR");
        ocultarColuna(tabelaVinculos, 0);
    }

    private void limparVinculo() {
        vinculoProfessorSelecionado = -1;
        limpar(txtVinculoTurma, txtVinculoDisciplina, txtVinculoProfessor);
        tabelaVinculos.clearSelection();
    }

    // =========================================================
    // AUXILIARES DE BANCO
    // =========================================================

    private int obterOuCriarSerie(Connection c, String nome) throws SQLException {
        String valor = nome.trim();

        int id = buscarSerieId(c, valor);
        if (id > 0) return id;

        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO serie (nome) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, valor);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return buscarSerieId(c, valor);
    }

    private int buscarSerieId(Connection c, String nome) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM serie WHERE nome=?")) {

            ps.setString(1, nome.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        return -1;
    }

    private int obterOuCriarTurma(Connection c, String nome, int serieId, int ano) throws SQLException {
        String valor = nome.trim().toUpperCase();

        int id = obterTurmaId(c, valor, serieId, ano);
        if (id > 0) return id;

        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO turmas (nome,id_serie,ano_letivo) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, valor);
            ps.setInt(2, serieId);
            ps.setInt(3, ano);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return obterTurmaId(c, valor, serieId, ano);
    }

    private int obterTurmaId(Connection c, String nome, int serieId, int ano) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("""
                SELECT id FROM turmas
                WHERE nome=? AND id_serie=? AND ano_letivo=?
        """)) {

            ps.setString(1, nome.trim().toUpperCase());
            ps.setInt(2, serieId);
            ps.setInt(3, ano);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        return -1;
    }

    private int buscarTurmaPorNome(Connection c, String nome) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM turmas WHERE nome=? ORDER BY ano_letivo DESC LIMIT 1")) {

            ps.setString(1, nome.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    private int obterOuCriarResponsavel(Connection c, String nome) throws SQLException {
        String valor = nome.trim();

        if (valor.isEmpty()) return -1;

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM responsavel WHERE nome=?")) {

            ps.setString(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO responsavel (nome) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, valor);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    private int buscarDisciplinaId(Connection c, String codigo) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM disciplinas WHERE codigo=?")) {

            ps.setString(1, codigo.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    private int buscarAlunoId(Connection c, String matricula) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM alunos WHERE matricula=?")) {

            ps.setString(1, matricula.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    private int buscarTrimestreId(Connection c, String nome) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM trimestre WHERE nome=?")) {

            ps.setString(1, nome.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        // Permite digitar somente 1, 2 ou 3.
        String numero = nome.trim();
        if (numero.matches("[123]")) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id FROM trimestre WHERE nome LIKE ?")) {

                ps.setString(1, numero + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    private int buscarProfessorIdPorNome(Connection c, String nome) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT id FROM professores WHERE nome=?")) {

            ps.setString(1, nome.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    // =========================================================
    // ATUALIZAÇÃO GERAL
    // =========================================================

    private void atualizarTudo() {
        try {
            atualizarAlunos();
            atualizarDisciplinas();
            atualizarProfessores();
            atualizarResponsaveis();
            atualizarSeries();
            atualizarTurmas();
            atualizarNotas();
            atualizarTrimestres();
            atualizarVinculos();
        } catch (Exception ex) {
            // Não interrompe a abertura da interface se o banco ainda
            // estiver sendo configurado.
            System.err.println("Erro ao atualizar dados: " + ex.getMessage());
        }
    }

    // =========================================================
    // INTERFACE / UTILITÁRIOS
    // =========================================================

    private JPanel painelBase(String titulo) {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBackground(BG);
        painel.setBorder(new EmptyBorder(10, 12, 12, 12));

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBorder(new EmptyBorder(0, 3, 5, 0));

        painel.add(label, BorderLayout.NORTH);
        return painel;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(190, 28));
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return t;
    }

    private JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(105, 28));
        return b;
    }

    private JPanel botoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p.setBackground(BG);
        return p;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 5, 3, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        return g;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints g, int linha, int coluna,
                                String rotulo, JTextField campo) {

        g.gridx = coluna * 2;
        g.gridy = linha;
        g.weightx = 0;
        painel.add(new JLabel(rotulo), g);

        g.gridx = coluna * 2 + 1;
        g.weightx = 1;
        painel.add(campo, g);
    }

    private JTable tabela() {
        JTable tabela = new JTable();
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabela.setRowHeight(24);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabela.setAutoCreateRowSorter(true);
        return tabela;
    }

    private void preencherTabela(JTable tabela, String sql, String... colunas) {
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] linha = new Object[colunas.length];

                for (int i = 0; i < colunas.length; i++) {
                    linha[i] = rs.getObject(i + 1);
                }

                modelo.addRow(linha);
            }

            tabela.setModel(modelo);
            tabela.getTableHeader().setReorderingAllowed(false);

        } catch (Exception ex) {
            System.err.println("Erro ao consultar tabela: " + ex.getMessage());
        }
    }

    private void ocultarColuna(JTable tabela, int indice) {
        if (tabela.getColumnModel().getColumnCount() <= indice) return;

        tabela.getColumnModel().getColumn(indice).setMinWidth(0);
        tabela.getColumnModel().getColumn(indice).setMaxWidth(0);
        tabela.getColumnModel().getColumn(indice).setPreferredWidth(0);
    }

    private boolean vazio(JTextField campo) {
        return campo == null || campo.getText().trim().isEmpty();
    }

    private void limpar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private int anoAtual() {
        return java.time.Year.now().getValue();
    }

    private String dataSql(String data) {
        String[] partes = data.split("/");

        if (partes.length != 3) {
            throw new IllegalArgumentException("Data inválida.");
        }

        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);

        // Validação simples usando LocalDate.
        java.time.LocalDate dataValida = java.time.LocalDate.of(ano, mes, dia);

        return dataValida.toString();
    }

    private void mensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto, "GestEdu",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void aviso(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Atenção",
                JOptionPane.WARNING_MESSAGE);
    }

    private void erro(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Erro",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean confirmar(String texto) {
        return JOptionPane.showConfirmDialog(
                this,
                texto,
                "GestEdu",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new GestEdu().setVisible(true);
        });
    }
}
