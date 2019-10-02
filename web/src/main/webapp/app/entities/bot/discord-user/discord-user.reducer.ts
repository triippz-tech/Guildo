import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IDiscordUser, defaultValue } from 'app/shared/model/bot/discord-user.model';

export const ACTION_TYPES = {
  FETCH_DISCORDUSER_LIST: 'discordUser/FETCH_DISCORDUSER_LIST',
  FETCH_DISCORDUSER: 'discordUser/FETCH_DISCORDUSER',
  CREATE_DISCORDUSER: 'discordUser/CREATE_DISCORDUSER',
  UPDATE_DISCORDUSER: 'discordUser/UPDATE_DISCORDUSER',
  DELETE_DISCORDUSER: 'discordUser/DELETE_DISCORDUSER',
  RESET: 'discordUser/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDiscordUser>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DiscordUserState = Readonly<typeof initialState>;

// Reducer

export default (state: DiscordUserState = initialState, action): DiscordUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DISCORDUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DISCORDUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DISCORDUSER):
    case REQUEST(ACTION_TYPES.UPDATE_DISCORDUSER):
    case REQUEST(ACTION_TYPES.DELETE_DISCORDUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DISCORDUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DISCORDUSER):
    case FAILURE(ACTION_TYPES.CREATE_DISCORDUSER):
    case FAILURE(ACTION_TYPES.UPDATE_DISCORDUSER):
    case FAILURE(ACTION_TYPES.DELETE_DISCORDUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDUSER_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DISCORDUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_DISCORDUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DISCORDUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/discord-users';

// Actions

export const getEntities: ICrudGetAllAction<IDiscordUser> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DISCORDUSER_LIST,
    payload: axios.get<IDiscordUser>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDiscordUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DISCORDUSER,
    payload: axios.get<IDiscordUser>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDiscordUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DISCORDUSER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDiscordUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DISCORDUSER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDiscordUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DISCORDUSER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
