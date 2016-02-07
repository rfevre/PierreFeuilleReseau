package fr.lille1.iut.pierrefeuillereseau;

public class Partie implements Comparable<Partie> {
  private String createur;
  private String challenger;
  private String id;
  private int nbJoueurs;
  private int nbManches;
  private String choixJ1;
  private String choixJ2;
  private int scoreJ1;
  private int scoreJ2;

  public Partie(String cr, String ch, String id, int nbJ, int nbM) {
    this.createur = cr;
    this.challenger = ch;
    this.id = id;
    this.nbJoueurs = nbJ;
    this.nbManches = nbM;
  }

  public Partie(String cr, String id, int nbJ, int nbM) {
    this(cr, null, id, nbJ, nbM);
  }

  // Getters / Setters :

  public String getCreateur() {
    return createur;
  }

  public void setCreateur(String c) {
    this.createur = c;
  }

  public String getChallenger() {
    return challenger;
  }

  public void setChallenger(String c) {
    this.challenger = c;
  }

  public String getId() {
    return id;
  }

  public void setId(String i) {
    this.id = i;
  }

  public int getNbJoueurs() {
    return nbJoueurs;
  }

  public void setNbJoueurs(int n) {
    this.nbJoueurs = n;
  }

  public int getNbManches() {
    return nbManches;
  }

  public void setNbManches(int n) {
    this.nbManches = n;
  }

  public String getChoixJ1() {
    return choixJ1;
  }

  public void setChoixJ1(String c) {
    choixJ1 = c;
  }

  public String getChoixJ2() {
    return choixJ2;
  }

  public void setChoixJ2(String c) {
    choixJ2 = c;
  }

  public int getScoreJ1() {
    return scoreJ1;
  }

  public void setScoreJ1(int s) {
    this.scoreJ1 = s;
  }

  public int getScoreJ2() {
    return scoreJ2;
  }

  public void setScoreJ2(int s) {
    this.scoreJ2 = s;
  }

  // Fin Getters / Setters

  public String toString() {
    return "Createur : " + getCreateur() + ", challenger : " + getChallenger();
  }

  public int compareTo(Partie o){
    System.out.println(o.getId());
    return o.getId().equals(getId()) ? 0 : -1;
  }
}
