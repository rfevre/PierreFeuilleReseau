package fr.lille1.iut.pierrefeuillereseau;

public class Partie {
  private String createur;
  private String challenger;
  private String id;
  private int nbJoueurs;
  private int nbManches;

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

  // Fin Getters / Setters

  public String toString() {
    return "Partie : \nCréée par :\t" + this.getCreateur()
    + "; Challenger :\t"+this.getChallenger()
    + "; Id : \t"+this.getId()
    + "; Nb Manches :\t"+this.getNbManches()
    + "; Nb Joueurs : \t" + this.getNbJoueurs();
  }
}
