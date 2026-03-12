Drop database if exists gestedu_v1_0;

create database gestedu_v1_0;

use gestedu_v1_0;

create table Alunos (
id int auto_increment not null primary key,
matricula varchar(10) not null unique,
nome varchar (125) not null,
cpf varchar(11) not null unique,
data_nascimento date not null,
id_turma int not null,
id_responsavel_1 int,
id_responsavel_2 int,
telefone varchar(15) not null,
email varchar(80)
);

--- Trigger para gerar automaticamente o número de matrícula do aluno
delimiter $$
create trigger trg_gera_matricula before insert on Alunos
for each row begin
if new.matricula is null or new.matricula = '' then 
set new.matricula = lpad(floor(rand() * 10000000000), 10, '0');
end if; end $$
delimiter ;

create table Responsavel (
id int auto_increment not null primary key,
nome varchar (125) not null,
cpf varchar(11) not null unique,
telefone varchar(15) not null,
email varchar(80),
endereco varchar(45),
cep_local varchar(8) not null
);

alter table Alunos add constraint fk_aluno_responsavel_1
foreign key (id_responsavel_1) references responsavel(id);

alter table Alunos add constraint fk_aluno_responsavel_2
foreign key (id_responsavel_2) references responsavel(id);

create table Turmas (
id int auto_increment not null primary key,
nome char(1) not null,
id_serie int not null,
ano_letivo int not null
);

alter table Alunos add constraint fk_aluno_turma
foreign key (id_turma) references turmas(id);

create table Serie (
id int auto_increment not null primary key,
nome char(2) not null
);

alter table Turmas add constraint fk_turma_serie
foreign key (id_serie) references serie(id);

create table Trimestre (
id int auto_increment not null primary key,
nome char(1) not null
);

create table Professores (
id int auto_increment not null primary key,
nome varchar (125) not null,
telefone varchar(15) not null
);

create table Disciplinas (
id int auto_increment not null primary key,
nome varchar (40) not null,
descricao text not null
);

create table liga_professor_disciplina (
id int auto_increment not null primary key,
id_professor int not null,
id_disciplina int not null
);

alter table liga_professor_disciplina add constraint fk_professor_disciplina
foreign key (id_professor) references professores(id);

alter table liga_professor_disciplina add constraint fk_disciplina_professor
foreign key (id_disciplina) references Disciplinas(id);

create table Nota_trimestral (
id int auto_increment not null primary key,
id_aluno int not null,
id_disciplina int not null,
id_trimestre int not null,
nota decimal(5,2)
);

alter table Nota_trimestral add constraint fk_nota_aluno
foreign key (id_aluno) references alunos(id);

alter table Nota_trimestral add constraint fk_nota_disciplina
foreign key (id_disciplina) references Disciplinas(id);

alter table Nota_trimestral add constraint fk_nota_trimestre
foreign key (id_trimestre) references Trimestre(id);

create table liga_turma_disciplina (
id int auto_increment not null primary key,
id_turma int not null,
id_disciplina int not null
);

alter table liga_turma_disciplina add constraint fk_turma_disciplina
foreign key (id_turma) references Turmas(id);

alter table liga_turma_disciplina add constraint fk_disciplina_turma
foreign key (id_disciplina) references Disciplinas(id);


create table liga_turma_disciplina_professor (
id int auto_increment not null primary key,
id_turma int not null,
id_disciplina int not null,
id_professor int not null
);

alter table liga_turma_disciplina_professor add constraint fk_liga_turma_disciplina_professor_turma
foreign key (id_turma) references Turmas(id);

alter table liga_turma_disciplina_professor add constraint fk_liga_turma_disciplina_professor_disciplina
foreign key (id_disciplina) references Disciplinas(id);

alter table liga_turma_disciplina_professor add constraint fk_liga_turma_disciplina_professor_professor
foreign key (id_professor) references professores(id);

--- Evita repetir a mesma combinação: turma + disciplina + professor
ALTER TABLE liga_turma_disciplina_professor
ADD CONSTRAINT uq_turma_disciplina_professor
UNIQUE (id_turma, id_disciplina, id_professor);

--- Confirma que a nota está entre 0 e 10
ALTER TABLE Nota_trimestral
ADD CONSTRAINT chk_nota_valida
CHECK (nota BETWEEN 0 AND 10);

insert into serie (nome) values (1), (2), (3), (10), (5);

select * from serie;

insert into trimestre (nome) values (1), (2), (3), (8);

select * from trimestre;

insert into Professores (nome, telefone) values 
('Luis Carlos', 48991234567), 
('Maria Del Carmen', 48998765432), 
('Jose Vargas', 48984563245), 
('Daniel Martines', 48976543245), 
('Sara Bracho', 48987561228),
('Julio Basanta', 48991245125);

select * from Professores;

insert into Disciplinas (nome, descricao) values
('Português', 'A disciplina de Português desenvolve a capacidade de comunicação por meio da leitura, interpretação e produção de textos, além do estudo da gramática e da literatura, ajudando os alunos a compreender e usar a língua de forma clara e eficaz.'),
('Matemática', 'Matemática trabalha com números, cálculos, formas e medidas, estimulando o raciocínio lógico e a resolução de problemas, sendo essencial para compreender situações do cotidiano e para diversas áreas do conhecimento.'),
('História', 'História analisa os acontecimentos do passado e suas consequências, permitindo que os alunos entendam a formação das sociedades, culturas e instituições, além de refletirem sobre o presente e o futuro.'),
('Geografia', 'Geografia estuda o espaço físico e social, abordando temas como relevo, clima, população e economia, ajudando os estudantes a compreender a relação entre o ser humano e o ambiente em diferentes regiões do mundo.'),
('Ciências', 'Em breve...'),
('Espanhol', 'Em breve...'),
('Filosofia', 'Em breve...');

select * from Disciplinas;

insert into Responsavel (nome, cpf, telefone, email, endereco, cep_local) values 
('Marcos Rivas', 25896325885, 48159632578, 'marcosrivas@gmail.com','São José', '88133258'), 
('Maria Murcia', 78951263996, 95887489632, 'mariamurcia@gmail.com', 'Palhoça', '88133248'), 
('Jose Prieto', 25896574885, 21559633295, 'joseprieto@gmail.com', 'Florianópolis', '88173258'), 
('Daniel Farias', 02825897485, 11997485236, 'danielfarias@gmamil.com', 'São José', '98133258'),
('Marcos Bracho', 14789632588, 58996325974, 'marcosbracho@gmail.com', 'Palhoça', '88133608');

select * from Responsavel;

insert into turmas (nome, id_serie, ano_letivo) values 
('A', 1, 2024),
('B', 1, 2025), 
('A', 2, 2025), 
('B', 2, 2025),
('A', 3, 2025),
('A', 1, 2022),
('A', 2, 2022),
('A', 3, 2022);

select * from turmas;

insert into alunos 
(nome, cpf, data_nascimento, id_turma, id_responsavel_1, id_responsavel_2, telefone, email) values 
('Maria José', 21421821921, '2008-02-01', 1, 1, 2, 48993692585, 'mariajose@gmail.com'),
('Daniel Aguilera', 31180967252, '2007-03-01', 2, 1, 2, 95999632587, 'danielaguilera@gmail.com'), 
('Marta Del Carmen', 42118291212, '2006-04-01', 3, 3, null, 47988259632, 'martadelcarmen@gmail.com'), 
('José Rodriguez', 21428129121, '2009-05-01', 4, 4, null, 21992587436, 'joserodriguez@gmail.com'),
('Lúcia Da Silva', 31125896332, '2001-06-01', 5, 5, null, 48991263960, 'luciadasilva@gmail.com');

select * from alunos;

insert into liga_professor_disciplina (id_professor, id_disciplina) values
(1, 1),
(1, 2),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(5, 1);

select * from liga_professor_disciplina;

insert into liga_turma_disciplina (id_turma, id_disciplina) values
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5);

select * from liga_turma_disciplina;

insert into liga_turma_disciplina_professor (id_turma, id_disciplina, id_professor) values
(1, 1, 1), (1, 2, 1), (1, 3, 3), (1, 4, 4), (1, 5, 5),
(2, 1, 1), (2, 2, 2), (2, 3, 3), (2, 4, 4), (2, 5, 5),
(3, 1, 1), (3, 2, 1), (3, 3, 3), (3, 4, 4), (3, 5, 5),
(4, 1, 1), (4, 2, 2), (4, 3, 3), (4, 4, 4), (4, 5, 5),
(5, 1, 1), (5, 2, 1), (5, 3, 3), (5, 4, 4), (5, 5, 5);

select * from liga_turma_disciplina_professor;

insert into nota_trimestral (id_aluno, id_disciplina, id_trimestre, nota) values
(1, 1, 1, 8.5), (1, 2, 1,null), (1, 3, 1, 4.5), (1, 4, 1, 2.0), (1, 5, 1, 5),
(2, 1, 1,null), (2, 2, 1, 2.5), (2, 3, 1, 3.5), (2, 4, 1, 8.2), (2, 5, 1, 10),
(3, 1, 1, 3.5), (3, 2, 1,10.0), (3, 3, 1,null), (3, 4, 1, 5.0), (3, 5, 1, 1.0),
(4, 1, 1, 6.5), (4, 2, 1, 6.0), (4, 3, 1, 9.9), (4, 4, 1,null), (4, 5, 1, 9.8),
(5, 1, 1, 10.0), (5, 2, 1, 5.2), (5, 3, 1, 6.3), (5, 4, 1, 7.8), (5, 5, 1,null);

select * from nota_trimestral;

set sql_safe_updates = 0;

update alunos
set
nome = 'Marcos Mendoza'
where id=1;

select * from alunos;

update disciplinas
set
descricao = 'Ciências explora os fenômenos naturais e os seres vivos, englobando áreas como biologia, física e química, incentivando a curiosidade científica e a compreensão de como funciona o mundo natural.'
where id=5;

select * from disciplinas;

update professores
set
telefone = '95991269360'
where id=3;

select * from professores;

update nota_trimestral
set
nota = 10
where id=2;

select * from nota_trimestral;

update responsavel
set
endereco = 'Palhoça'
where id=3;

select * from responsavel;

update serie
set
nome = 4
where id=4;

select * from serie;

update trimestre
set
nome = 4
where id=4;

select * from trimestre;

update turmas
set
ano_letivo = 2025
where id=1;

select * from turmas;

DELETE FROM Nota_trimestral
WHERE nota IS null or id_aluno = 1 or id_aluno = 2;

select * from nota_trimestral;

DELETE FROM liga_turma_disciplina_professor
WHERE ID = 2;

select * from liga_turma_disciplina_professor;

DELETE FROM liga_turma_disciplina
WHERE ID = 5 or ID = 10 or ID = 15 or ID = 20 or ID = 25;

select * from liga_turma_disciplina;

DELETE FROM liga_professor_disciplina
WHERE ID = 7;

select * from liga_professor_disciplina;

DELETE FROM alunos
WHERE cpf = 21421821921 or cpf = 31180967252;

select * from alunos;

DELETE FROM turmas
WHERE ano_letivo = 2022;

select * from turmas;

DELETE FROM Responsavel
WHERE id = 1 or id = 2;

select * from Responsavel;

DELETE FROM Professores
WHERE nome = 'Julio Basanta';

select * from Professores;

DELETE FROM Disciplinas
WHERE descricao = 'Em breve...';

select * from Disciplinas;

DELETE FROM Trimestre
WHERE nome = 4;

select * from Trimestre;

DELETE FROM Serie
WHERE nome != 1 and nome != 2 and nome != 3;

select * from Serie;

set sql_safe_updates = 1;
