#ifndef READ_H_INCLUDED
#define READ_H_INCLUDED

#include <map>
#include <set>
#include <unordered_map>
#include <unordered_set>
#include <string>

#include "deck.h"

class Cards;
class Decks;
class Deck;

DeckList parse_deck_list(std::string list_string, Decks& decks);
void parse_card_spec(const Cards& cards, const std::string& card_spec, unsigned& card_id, unsigned& card_num, char& num_sign, char& mark);
const std::pair<std::vector<unsigned>, std::map<signed, char>> string_to_ids(const Cards& all_cards, const std::string& deck_string, const std::string & description);
unsigned load_custom_decks(Decks& decks, Cards& cards, const std::string & filename);
void read_owned_cards(Cards& cards, std::map<unsigned, unsigned>& owned_cards, const std::string & filename);
unsigned read_card_abbrs(Cards& cards, const std::string& filename);
unsigned read_bge_aliases(std::unordered_map<std::string, std::string> & bge_aliases, const std::string & filename);
std::unordered_map<unsigned, unsigned> read_custom_cards(Cards& all_cards, const std::string& filename, bool abort_on_missing);

// trim from start
static inline std::string &ltrim(std::string &s)
{
    s.erase(s.begin(), std::find_if(s.begin(), s.end(), std::not1(std::ptr_fun<int, int>(std::isspace))));
    return s;
}

// trim from end
static inline std::string &rtrim(std::string &s)
{
    s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), s.end());
    return s;
}

// trim from both ends
static inline std::string &trim(std::string &s)
{
    return ltrim(rtrim(s));
}

// is line should be skipped?
static inline bool is_line_empty_or_commented(std::string &s)
{
    return (s.size() == 0) || (strncmp(s.c_str(), "//", 2) == 0);
}

#endif
